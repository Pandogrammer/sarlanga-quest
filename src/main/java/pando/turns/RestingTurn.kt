package pando.turns

import pando.creatures.Creature

class RestingTurn {

    fun execute(creatures: List<Creature>) {
        creatures.forEach { restIfAlive(it) }
    }

    private fun restIfAlive(creature: Creature) {
        if (creature.health > 0) {
            creature.fatigue -= creature.speed
            if (creature.fatigue < 0) creature.fatigue = 0
        }
    }

}