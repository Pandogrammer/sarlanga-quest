package pando.turns

import pando.actions.Action
import pando.actions.ActionDie
import pando.creatures.Creature
import io.reactivex.subjects.PublishSubject
import pando.domain.ActionExecution

class CreatureAction(private val die: ActionDie) {
    val executed = PublishSubject.create<ActionExecution>()

    fun execute(creature: Creature, action: Action, target: Creature) {
        val roll = die.roll()
        when(roll) {
            10 -> {
                action.execute(creature, target, critical = true)
                creature.fatigue += action.fatigue
            }
            1 -> creature.fatigue = 3
            in creature.stats.dexterity .. 9 -> {
                action.execute(creature, target)
                creature.fatigue += action.fatigue
            }
            else -> creature.fatigue += action.fatigue
        }

        executed.onNext(ActionExecution(creature, action, target, roll))
    }
}

