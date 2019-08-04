package pando.actions

import pando.creatures.Creature

class FindFastestCreatures {

    fun execute(creatures: List<Creature>): List<Creature> {
        val maxSpeed = creatures.maxBy { it.stats.speed }!!.stats.speed
        return creatures.filter { it.stats.speed == maxSpeed }
    }

}