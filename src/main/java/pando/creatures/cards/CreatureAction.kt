package pando.creatures.cards

import pando.creatures.CreatureCardAction
import pando.creatures.CreatureCardAction.*

sealed class CreatureAction(val fatigue: Int,
                            val melee: Boolean) {
    abstract val code: CreatureCardAction
}

data class NormalAttack(override val code: CreatureCardAction = NORMAL_ATTACK): CreatureAction(1, true)

data class SlowAttack(override val code: CreatureCardAction = SLOW_ATTACK): CreatureAction(2, true)