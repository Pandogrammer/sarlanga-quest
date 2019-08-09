package pando.creatures.races

import pando.creatures.*
import pando.domain.Events

class Frog(events: Events, position: Position, team: Int) : Creature(FrogStats(), position, team) {
    override val behaviour = FrogBehaviour(this, events)
}

class FrogStats : CreatureStats(5, 3, 0, 2, 4, 1)

class FrogBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {

    private val tokenType = Token.FROG

    init {
        val reaction = events.damageEvent.filter{ it.target == creature && it.action.melee }.subscribe{
            it.actor.addTokens(tokenType, 1)
        }

        val extraDamage = events.damageEvent.filter{ it.actor == creature }.subscribe {
            val tokens = it.target.getTokens(tokenType)
            tokens?.let { tokens ->
                it.target.damageCounters += tokens
                it.target.removeTokens(tokenType, tokens)
            }
        }

    }
}