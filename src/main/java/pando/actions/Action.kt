package pando.actions

import pando.creatures.SpawnedCreature

abstract class Action(val fatigue: Int) {
    abstract val melee: Boolean
    abstract fun execute(spawnedCreature: SpawnedCreature, target: SpawnedCreature, critical: Boolean = false)
}