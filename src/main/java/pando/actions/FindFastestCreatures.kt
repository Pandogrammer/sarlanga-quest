package pando.actions

import pando.creatures.SpawnedCreature

class FindFastestCreatures {

    fun execute(spawnedCreatures: List<SpawnedCreature>): List<SpawnedCreature> {
        val maxSpeed = spawnedCreatures.maxBy { it.stats.speed }!!.stats.speed
        return spawnedCreatures.filter { it.stats.speed == maxSpeed }
    }

}