package pando.domain

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.*
import pando.creatures.CreatureCode.EYE
import pando.creatures.CreatureCode.SKELETON
import pando.creatures.races.Eye
import pando.turns.*

//esta clase pide refactor
class Match(playerCreatures: List<CreatureCode>,
            opponentCreatures: List<CreatureCode>,
            actionDie: ActionDie = ActionDie()){

    private val creatureAction = CreatureAction(actionDie)
    private val restingTurn = RestingTurn()

    val events = MatchEvents(
            actionsExecuted(),
            rests(),
            damage(),
            kills(),
            deaths()
            )

    private fun actionsExecuted() = creatureAction.executed
    private fun rests() = restingTurn.executed
    private fun damage() = PublishSubject.create<Damage>()
    private fun kills() = actionsExecuted().filter { it.target.health() == 0 }.map { Kill(it.creature) }
    private fun deaths() = actionsExecuted().filter { it.target.health() == 0 }.map { Death(it.target) }

    var activeCreature: Creature? = null
    val creatures = playerCreatures.map { spawnCreature(it, 1) } + opponentCreatures.map{ spawnCreature(it, 2) }

    //todas estas acciones se deberian inyectar/inlinear
    private val nextTurn = NextTurn()
    private val winnerValidation = WinnerValidation()

    init {
        activeCreature = FirstTurn().execute(creatures)
    }

    //esto tambien pide refactor :)
    fun creatureAction(objectiveId: Int) {
        activeCreature?.let {
            val action = Attack()
            creatureAction.execute(it, action, creatures[objectiveId])
            if (thereIsNoWinner()) {
                activeCreature = nextTurn.execute(creatures, it.team)
                while (activeCreature == null) {
                    restingTurn.execute(creatures)
                    activeCreature = nextTurn.execute(creatures, it.team)
                }
            }
        }
    }

    private fun thereIsNoWinner(): Boolean {
        val winner: Int? = winnerValidation.execute(creatures) ?: return true

        println("Team $winner won!")
        return false
    }


    private fun spawnCreature(code: CreatureCode, team: Int): Creature {
        return when(code){
            EYE -> Eye(events, Position(1, 1), team)
            SKELETON -> Eye(events, Position(1, 1), team)
        }
    }

}

//hecho solo para poder mockear. -_-
class MatchEvents(override val actions: Observable<ActionExecution>,
                  override val rest: Observable<Rest>,
                  override val damage: PublishSubject<Damage>,
                  override val kills: Observable<Kill>,
                  override val deaths: Observable<Death>) : Events

