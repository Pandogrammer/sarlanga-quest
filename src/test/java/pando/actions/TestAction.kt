package pando.actions

import pando.creatures.Creature

class TestAction(fatigue: Int) : Action(fatigue) {
    override fun execute(creature: Creature, target: Creature, critical: Boolean) {}
}