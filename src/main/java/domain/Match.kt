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

    fun creatureAction(creatureId: Int, objectiveId: Int) {
        if (activeCreature == creatures[creatureId]) {
            CreatureAction(actionDie).execute(creatures[creatureId], Attack(), creatures[objectiveId])
            lastTeamTurn = activeCreature!!.team
            if (WinnerValidation().execute(creatures) == null) {
                activeCreature = NextTurn().execute(creatures, lastTeamTurn)
                while(activeCreature == null){
                    RestingTurn().execute(creatures)
                    activeCreature = NextTurn().execute(creatures, lastTeamTurn)
                }
            }
        }
    }

}