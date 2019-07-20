package domain

import actions.ActionDie
import actions.Attack
import creatures.Creature
import io.reactivex.subjects.PublishSubject
import turns.*

class Match(val creatures: List<Creature>, private val actionDie: ActionDie = ActionDie()){
    var activeCreature: Creature? = null

    val creatureAction = CreatureAction(actionDie)
    val nextTurn = NextTurn()
    val restingTurn = RestingTurn()
    val winnerValidation = WinnerValidation()

    val turns = PublishSubject.create<Turn>()

    init {
        activeCreature = FirstTurn().execute(creatures)
    }

    fun creatureAction(objectiveId: Int) {
        activeCreature?.let {
            val action = Attack()
            creatureAction.execute(it, action, creatures[objectiveId])
            turns.onNext(Turn(team = it.team, creature = it, action = action))
            if (winnerValidation.execute(creatures) == null) {
                activeCreature = nextTurn.execute(creatures, it.team)
                while (activeCreature == null) {
                    restingTurn.execute(creatures)
                    activeCreature = nextTurn.execute(creatures, it.team)
                }
            }
        }
    }

}

class Turn(val team: Int, val creature: Creature, val action: Attack)