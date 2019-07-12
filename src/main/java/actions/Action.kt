package actions

import creatures.Creature

abstract class Action(val fatigue: Int) {
    abstract fun execute(creature: Creature, target: Creature, critical: Boolean = false)
}