package pando.test

import pando.actions.Action
import pando.creatures.SpawnedCreature

class TestAction(fatigue: Int, override val melee: Boolean = false) : Action(fatigue) {
    override fun execute(spawnedCreature: SpawnedCreature, target: SpawnedCreature, critical: Boolean) {}
}