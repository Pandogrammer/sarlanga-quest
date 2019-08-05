package pando.creatures.races

import pando.creatures.*
import pando.domain.Events


class Golem(events: Events, position: Position, team: Int) : Creature(GolemStats(), position, team) {
    override val behaviour = GolemBehaviour(this, events)
}

class GolemStats : CreatureStats(8, 2, 1, 1, 4, 1)

class GolemBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {

    init {
        events.rest.subscribe{ if(creature.fatigue == 0) addShield() }

        addShield()
    }

    private fun addShield() = creature.addTokens(Token.GOLEM, 1)
}

