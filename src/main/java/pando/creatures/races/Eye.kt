package pando.creatures.races

import pando.creatures.*
import pando.creatures.cards.EyeCard
import pando.domain.Events

class EyeStats : CreatureStats(4, 1, 0, 3, 3, 1)

class EyeBehaviour: CreatureBehaviour {
    override fun attachTo(spawnedCreature: SpawnedCreature, events: Events) {
        events.kills.filter{ it.killer == spawnedCreature }
                .subscribe{ spawnedCreature.attackBonus++ }
    }
}

