package pando.turns

import pando.actions.Action
import pando.actions.ActionDie
import pando.creatures.SpawnedCreature
import io.reactivex.subjects.PublishSubject
import pando.domain.ActionExecution

class ExecuteAction(private val die: ActionDie) {
    val executed = PublishSubject.create<ActionExecution>()

    fun execute(spawnedCreature: SpawnedCreature, action: Action, target: SpawnedCreature) {
        val roll = die.roll()
        when(roll) {
            10 -> {
                action.execute(spawnedCreature, target, critical = true)
                spawnedCreature.fatigue += action.fatigue
            }
            1 -> spawnedCreature.fatigue = 3
            in spawnedCreature.stats.dexterity .. 9 -> {
                action.execute(spawnedCreature, target)
                spawnedCreature.fatigue += action.fatigue
            }
            else -> spawnedCreature.fatigue += action.fatigue
        }

        executed.onNext(ActionExecution(spawnedCreature, action, target, roll))
    }
}

