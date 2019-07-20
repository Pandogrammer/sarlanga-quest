package pando.domain

import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.Creature
import pando.creatures.CreatureCode
import pando.creatures.CreatureCode.EYE
import pando.creatures.CreatureCode.SKELETON
import pando.creatures.Eye
import pando.creatures.Skeleton
import pando.turns.*

class Match(playerCreatures: List<CreatureCode>,
            opponentCreatures: List<CreatureCode>,
            actionDie: ActionDie = ActionDie()){

    private val creatureAction = CreatureAction(actionDie)
    val actions = creatureAction.executed

    val deaths = actions.filter{ it.target.health == 0 }.map{ Death(it.creature, it.target)}

    var activeCreature: Creature? = null
    val creatures = playerCreatures.map { spawnCreature(it, 1) } + opponentCreatures.map{ spawnCreature(it, 2) }

    //todas estas acciones se deberian inyectar/inlinear
    private val nextTurn = NextTurn()
    private val restingTurn = RestingTurn()
    private val winnerValidation = WinnerValidation()



    init {
        activeCreature = FirstTurn().execute(creatures)
    }

    fun creatureAction(objectiveId: Int) {
        activeCreature?.let {
            val action = Attack()
            creatureAction.execute(it, action, creatures[objectiveId])
            if (winnerValidation.execute(creatures) == null) {
                activeCreature = nextTurn.execute(creatures, it.team)
                while (activeCreature == null) {
                    restingTurn.execute(creatures)
                    activeCreature = nextTurn.execute(creatures, it.team)
                }
            }
        }
    }


    private fun spawnCreature(code: CreatureCode, team: Int): Creature {
        return when(code){
            EYE -> Eye(team, deaths)
            SKELETON -> Skeleton(team)
        }
    }

}

class Death(val killer: Creature, val killed: Creature)