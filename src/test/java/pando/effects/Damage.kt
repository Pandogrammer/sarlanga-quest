package pando.effects

import pando.creatures.Creature

class Damage(val value: Int) {

    fun applyOn(creature: Creature) {
        creature.damageCounters =+ value
    }

}