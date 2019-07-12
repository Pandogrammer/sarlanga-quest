package domain

import actions.ActionDie
import actions.Attack
import creatures.Creature
import turns.*

class Match (val creatures: List<Creature>){

    val actionDie = ActionDie()
    var lastTeamTurn: Int = -1
    var activeCreature: Creature? = null

    init {
        activeCreature = FirstTurn().execute(creatures)
    }

    fun creatureAction(objectiveId: Int) {
        activeCreature?.let {
            CreatureAction(actionDie).execute(it, Attack(), creatures[objectiveId])
            lastTeamTurn = it.team
            if (WinnerValidation().execute(creatures) == null) {
                activeCreature = NextTurn().execute(creatures, lastTeamTurn)
                while (activeCreature == null) {
                    RestingTurn().execute(creatures)
                    activeCreature = NextTurn().execute(creatures, lastTeamTurn)
                }
            }
        }
    }

}