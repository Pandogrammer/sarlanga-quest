package pando.effects

import pando.creatures.SpawnedCreature

class Damage(val value: Int) {

    fun applyOn(spawnedCreature: SpawnedCreature) {
        spawnedCreature.damageCounters =+ value
    }

}