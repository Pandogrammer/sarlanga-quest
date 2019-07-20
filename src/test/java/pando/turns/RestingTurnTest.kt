package pando.turns

import pando.actions.ActionDie
import pando.actions.TestAction
import com.nhaarman.mockitokotlin2.mock
import pando.creatures.Creature
import org.junit.Test
import kotlin.test.assertEquals

class RestingTurnTest {

    val actionDie : ActionDie = mock()

    @Test
    fun `given no active creatures, all living creatures rest by its speed`(){
        val restingTurn = RestingTurn()
        val creature = Creature(speed = 2, dexterity = 3)
        val action = TestAction(fatigue = 3)

        CreatureAction(actionDie).execute(creature, action, creature)
        restingTurn.execute(listOf(creature))

        assertEquals(1, creature.fatigue)
    }

    @Test
    fun `given creature is dead, it should not rest`(){
        val restingTurn = RestingTurn()
        val creature = Creature(initialHealth = 0, speed = 2, dexterity = 3)
        val action = TestAction(fatigue = 3)

        CreatureAction(actionDie).execute(creature, action, creature)
        restingTurn.execute(listOf(creature))

        assertEquals(3, creature.fatigue)
    }
}

