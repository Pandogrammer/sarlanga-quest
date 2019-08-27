package pando.creatures.races

import pando.creatures.*
import pando.creatures.cards.SkeletonCard
import pando.domain.Events

class SkeletonStats : CreatureStats(6, 2, 0, 1, 4, 1)

class SkeletonBehaviour : CreatureBehaviour {
    override fun attachTo(spawnedCreature: SpawnedCreature, events: Events) {

        val resurrection = events.deaths.filter { it.spawnedCreature == spawnedCreature }.subscribe{
            spawnedCreature.fatigue = 7
            events.rest.takeUntil{ spawnedCreature.fatigue == 0 }.subscribe {
                spawnedCreature.fatigue -= 1
                if (spawnedCreature.fatigue == 0){
                    spawnedCreature.damageCounters = 0
                }
            }
        }
    }
}
