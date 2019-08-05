package pando.creatures.races

import pando.creatures.*
import pando.domain.Events


class Fluffy(events: Events, position: Position, team: Int) : Creature(FluffyStats(), position, team) {
    override val behaviour = FluffyBehaviour(this, events)
}

class FluffyStats: CreatureStats(6, 3, 0, 1, 4, 1)

class FluffyBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {
    var killCounter = 0

    init{
        events.kills.filter { it.killer == creature }.subscribe {
            killCounter++
            creature.damageCounters -= killCounter

            if(creature.damageCounters < 0 ) creature.damageCounters = 0
        }
    }
}
