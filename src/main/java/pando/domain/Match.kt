package pando.domain

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.*
import pando.creatures.CreatureCode.EYE
import pando.creatures.CreatureCode.SKELETON
import pando.creatures.races.Eye
import pando.creatures.races.Skeleton
import pando.turns.*
import kotlin.random.Random

class Match(playerCreatures: Map<Position, CreatureCode>,
            opponentCreatures: Map<Position, CreatureCode>,
            actionDie: ActionDie = ActionDie()){

    private val creatureAction = CreatureAction(actionDie)
    private val restingTurn = RestingTurn()
    private val nextTurn = NextTurn()
    private val winnerValidation = WinnerValidation()

    private val actionsExecuted = creatureAction.executed
    private val rests = restingTurn.executed
    private val damage = actionsExecuted.filter { it.roll >= it.actor.stats.dexterity }.map { DamageEvent(it.actor, it.action, it.target) } //asco
    private val kills = actionsExecuted.filter { it.target.health() == 0 }.map { Kill(it.actor) }
    private val deaths = actionsExecuted.filter { it.target.health() == 0 }.map { Death(it.target) }

    val messages = PublishSubject.create<String>()

    val events = MatchEvents(
            actionsExecuted,
            rests,
            damage,
            kills,
            deaths
    )

    var activeCreature: Creature? = null
    var winner: Int? = null

    val creatures = playerCreatures.map { spawnCreature(it, 1) } +
                    opponentCreatures.map{ spawnCreature(it, 2) }


    fun start() {
        messagesSubscriptions()
        messages.onNext("Match started.")
        activeCreature = FirstTurn().execute(creatures)

        messageActiveCreature()
        iaTurn()
    }

    private fun iaTurn() {
        activeCreature?.let {
            if(it.team == 2) {
                val action = Attack()
                val possibleTargets = creatures.filter { it.team == 1 }
                var objective = Random.nextInt(0, possibleTargets.size)
                while (!validate(action, objective)) {
                    objective = Random.nextInt(0, possibleTargets.size)
                }

                actionExecution(action, objective)
            }
        }
    }

    private fun messagesSubscriptions() {
        messages.subscribe{ println(it) }

        actionsExecuted.subscribe { println("${it.actor} executed ${it.action.javaClass.simpleName} on ${it.target}, rolling: ${it.roll}")}

        deaths.subscribe { println("${it.creature} died")}

        rests.subscribe { println("Resting turn.") }
    }

    fun validate(action: Action, objectiveId: Int) : Boolean {
        if(action.melee) {
            if (HasBlockers().execute(creatures[objectiveId], creatures)) return false
        }
        return true
    }

    //refactorizame pls :c
    fun actionExecution(action: Action, objectiveId: Int) {
        activeCreature?.let {
            creatureAction.execute(it, action, creatures[objectiveId])
            if (thereIsNoWinner()) {
                activeCreature = nextTurn.execute(creatures, it.team)
                while (activeCreature == null) {
                    restingTurn.execute(creatures)
                    activeCreature = nextTurn.execute(creatures, it.team)
                }

                messageStatus()
                messageActiveCreature()
                iaTurn()
            }
        }
    }

    private fun messageStatus() {
        var map = ""
        val playerCreatures = creatures.filter { it.team == 1 }
        val iaCreatures = creatures.filter { it.team == 2 }

        playerCreatures.forEach { map += if(it.health() > 0) "o " else "x " }
        map += "\n-----------\n"
        iaCreatures.forEach { map += if(it.health() > 0) "o " else "x " }
        messages.onNext(map)
    }

    private fun messageActiveCreature() {
        activeCreature?.let { messages.onNext("Active creature: $it") }
    }

    private fun thereIsNoWinner(): Boolean {
        winner = winnerValidation.execute(creatures) ?: return true

        activeCreature = null
        messages.onNext("Team $winner won!")
        return false
    }


    private fun spawnCreature(creature: Map.Entry<Position, CreatureCode>, team: Int): Creature {
        return when(creature.value){
            EYE -> Eye(events, creature.key, team)
            SKELETON -> Skeleton(events, creature.key, team)
        }
    }

}

//hecho solo para poder mockear. -_-
class MatchEvents(override val actions: Observable<ActionExecution>,
                  override val rest: Observable<Rest>,
                  override val damageEvent: Observable<DamageEvent>,
                  override val kills: Observable<Kill>,
                  override val deaths: Observable<Death>) : Events

