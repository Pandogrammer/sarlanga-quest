package pando.test

import pando.actions.Action
import pando.creatures.Creature

class TestAction(fatigue: Int, override val melee: Boolean = false) : Action(fatigue) {
    override fun execute(creature: Creature, target: Creature, critical: Boolean) {}
}