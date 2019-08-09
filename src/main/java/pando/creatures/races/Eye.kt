package pando.creatures.races

import pando.creatures.*
import pando.domain.Events

class Eye(events: Events, position: Position, team: Int) : Creature(EyeStats(), position, team) {
    override val behaviour = EyeBehaviour(this, events)
}

class EyeStats : CreatureStats(4, 1, 0, 3, 3, 1)

class EyeBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {
    init {
        val damageBoost = events.kills.filter{ it.killer == creature }
                    .subscribe{ creature.attackBonus++ }
    }
}

