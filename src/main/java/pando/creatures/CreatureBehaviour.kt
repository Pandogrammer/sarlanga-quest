package pando.creatures

import pando.domain.Events

interface CreatureBehaviour {
    fun attachTo(spawnedCreature: SpawnedCreature, events: Events)
}
