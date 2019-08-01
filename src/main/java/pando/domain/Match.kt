package pando.domain

import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.Creature
import pando.creatures.CreatureCode
import pando.creatures.CreatureCode.EYE
import pando.creatures.CreatureCode.SKELETON
import pando.creatures.Eye
import pando.creatures.Skeleton
import pando.turns.*

//esta clase pide refactor
class Match(playerCreatures: List<CreatureCode>,
            opponentCreatures: List<CreatureCode>,
            actionDie: ActionDie = ActionDie()){

    private val creatureAction = CreatureAction(actionDie)
    private val restingTurn = RestingTurn()

    val actions = creatureAction.executed
    val restingTurns = restingTurn.executed

    val kills = actions.filter{ it.target.health() == 0 }.map{ Kill(it.creature)}
    val deaths = actions.filter{ it.target.health() == 0 }.map { Death(it.target) }

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
            EYE -> Eye(team, kills)
            SKELETON -> Skeleton(team, restingTurns, deaths)
        }
    }

}

