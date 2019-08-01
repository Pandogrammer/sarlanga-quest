package pando.actions

import pando.creatures.Creature

abstract class Action(val fatigue: Int) {
    abstract val melee: Boolean
    abstract fun execute(creature: Creature, target: Creature, critical: Boolean = false)
}