package pando.domain

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.*
import pando.creatures.CreatureCode.*
import pando.creatures.cards.CreatureCard
import pando.creatures.races.*
import pando.turns.*
import kotlin.random.Random

class Match(playerCreatures: Map<Position, CreatureCard>,
            opponentCreatures: Map<Position, CreatureCard>,
            actionDie: ActionDie = ActionDie()) {

    private val creatureAction = ExecuteAction(actionDie)
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

    var activeSpawnedCreature: SpawnedCreature? = null
    var winner: Int? = null

    val spawnedCreatures = spawnCreatures(playerCreatures, opponentCreatures)

    private fun spawnCreatures(playerCreatures: Map<Position, CreatureCard>,
                               opponentCreatures: Map<Position, CreatureCard>): List<SpawnedCreature> {
        val creatures = ArrayList<SpawnedCreature>()
        var id = 0
        playerCreatures.forEach{
            val behaviour = getBehaviour(it.value.code)
            val stats = getStats(it.value.code)
            val spawned = SpawnedCreature(id, it.key, 1, stats, behaviour, it.value)
            behaviour.attachTo(spawned, events)
            creatures.add(spawned)
            id++
        }

        opponentCreatures.forEach{
            val behaviour = getBehaviour(it.value.code)
            val stats = getStats(it.value.code)
            val spawned = SpawnedCreature(id, it.key, 2, stats, behaviour, it.value)
            behaviour.attachTo(spawned, events)
            creatures.add(spawned)
            id++
        }

        return creatures
    }

    fun start() {
        messagesSubscriptions()
        messages.onNext("Match started.")
        activeSpawnedCreature = FirstTurn().execute(spawnedCreatures)

        messageActiveCreature()
        iaTurn()
    }

    private fun iaTurn() {
        activeSpawnedCreature?.let {
            if (it.team == 2) {
                val action = Attack()
                val possibleTargets = spawnedCreatures.filter { it.team == 1 }
                var objective = Random.nextInt(0, possibleTargets.size)
                while (!validate(action, objective)) {
                    objective = Random.nextInt(0, possibleTargets.size)
                }

                actionExecution(action, objective)
            }
        }
    }

    private fun messagesSubscriptions() {
        messages.subscribe { println(it) }

        actionsExecuted.subscribe { println("${it.actor} executed ${it.action.javaClass.simpleName} on ${it.target}, rolling: ${it.roll}") }

        deaths.subscribe { println("${it.spawnedCreature} died") }

        rests.subscribe { println("Resting turn.") }
    }

    fun validate(action: Action, objectiveId: Int): Boolean {
        if (action.melee) {
            if (HasBlockers().execute(spawnedCreatures[objectiveId], spawnedCreatures)) return false
        }
        return true
    }

    //refactorizame pls :c
    fun actionExecution(action: Action, objectiveId: Int) {
        activeSpawnedCreature?.let {
            creatureAction.execute(it, action, spawnedCreatures[objectiveId])
            if (thereIsNoWinner()) {
                activeSpawnedCreature = nextTurn.execute(spawnedCreatures, it.team)
                while (activeSpawnedCreature == null) {
                    restingTurn.execute(spawnedCreatures)
                    activeSpawnedCreature = nextTurn.execute(spawnedCreatures, it.team)
                }

                messageStatus()
                messageActiveCreature()
                iaTurn()
            }
        }
    }

    private fun messageStatus() {
        var map = ""
        val playerCreatures = spawnedCreatures.filter { it.team == 1 }
        val iaCreatures = spawnedCreatures.filter { it.team == 2 }

        playerCreatures.forEach { map += if (it.health() > 0) "o " else "x " }
        map += "\n-----------\n"
        iaCreatures.forEach { map += if (it.health() > 0) "o " else "x " }
        messages.onNext(map)
    }

    private fun messageActiveCreature() {
        activeSpawnedCreature?.let { messages.onNext("Active creature: $it") }
    }

    private fun thereIsNoWinner(): Boolean {
        winner = winnerValidation.execute(spawnedCreatures) ?: return true

        activeSpawnedCreature = null
        messages.onNext("Team $winner won!")
        return false
    }



    private fun getBehaviour(creature: CreatureCode): CreatureBehaviour {
        return when (creature) {
            EYE -> EyeBehaviour()
            SKELETON -> SkeletonBehaviour()
            FLUFFY -> FluffyBehaviour()
            FROG -> FrogBehaviour()
            GOLEM -> GolemBehaviour()
        }
    }

    private fun getStats(creature: CreatureCode): CreatureStats {
        return when (creature) {
            EYE -> EyeStats()
            SKELETON -> SkeletonStats()
            FLUFFY -> FluffyStats()
            FROG -> FrogStats()
            GOLEM -> GolemStats()
        }
    }

}

//hecho solo para poder mockear. -_-
class MatchEvents(override val actions: Observable<ActionExecution>,
                  override val rest: Observable<Rest>,
                  override val damageEvent: Observable<DamageEvent>,
                  override val kills: Observable<Kill>,
                  override val deaths: Observable<Death>) : Events

