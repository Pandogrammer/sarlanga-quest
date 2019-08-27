package pando.creatures.races

import pando.creatures.*
import pando.creatures.cards.FluffyCard
import pando.domain.Events

class FluffyStats: CreatureStats(6, 3, 0, 1, 4, 1)

class FluffyBehaviour : CreatureBehaviour {
    var killCounter = 0

    override fun attachTo(spawnedCreature: SpawnedCreature, events: Events) {
        val healing = events.kills.filter { it.killer == spawnedCreature }.subscribe {
            killCounter++
            spawnedCreature.damageCounters -= killCounter

            if(spawnedCreature.damageCounters < 0 ) spawnedCreature.damageCounters = 0
        }
    }
}
