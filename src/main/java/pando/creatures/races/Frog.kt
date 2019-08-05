package pando.creatures.races

import pando.creatures.*
import pando.domain.Events

class Frog(events: Events, position: Position, team: Int) : Creature(FrogStats(), position, team) {
    override val behaviour = FrogBehaviour(this, events)
}

class FrogStats : CreatureStats(5, 3, 0, 2, 4, 1)

class FrogBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {
    init {
        events.damage.filter{ it.target == creature && it.action.melee }.subscribe{
            it.actor.addTokens(Token.FROG, 1)
        }
    }
}