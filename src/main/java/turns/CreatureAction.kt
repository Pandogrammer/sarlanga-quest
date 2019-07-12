package turns

import actions.Action
import actions.ActionDie
import creatures.Creature

class CreatureAction(val die: ActionDie) {

    fun execute(creature: Creature, action: Action, target: Creature) {
        val roll = die.roll()

        when(roll) {
            10 -> {
                action.execute(creature, target, critical = true)
                creature.fatigue += action.fatigue
            }
            1 -> creature.fatigue = 3
            in creature.dexterity .. 9 -> {
                action.execute(creature, target)
                creature.fatigue += action.fatigue
            }
            else -> creature.fatigue += action.fatigue
        }
    }


}