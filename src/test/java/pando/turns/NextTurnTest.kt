package pando.turns

import pando.actions.ActionDie
import pando.actions.TestAction
import com.nhaarman.mockitokotlin2.mock
import pando.creatures.Creature
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NextTurnTest {

    val actionDie: ActionDie = mock()

    @Test
    fun `given creature actions, next fastest creature becomes active`() {
        val nextTurn = NextTurn()
        val a = Creature(speed = 3, dexterity = 3)
        val b = Creature(speed = 2, dexterity = 3)
        val creatures = listOf(a, b)
        val action = TestAction(fatigue = 3)

        val firstTurnCreature = FirstTurn().execute(creatures)
        val creatureAction = CreatureAction(actionDie).execute(firstTurnCreature, action, a)
        val nextTurnCreature = nextTurn.execute(creatures, firstTurnCreature.team)

        assertEquals(b, nextTurnCreature)
    }


    @Test
    fun `given creature of one team actions, next fastest creature of the other team becomes active`() {
        val nextTurn = NextTurn()
        val a = Creature(speed = 3, team = 1, dexterity = 3)
        val b = Creature(speed = 2, team = 1, dexterity = 3)
        val c = Creature(speed = 2, team = 2, dexterity = 3)
        val creatures = listOf(a, b, c)
        val action = TestAction(fatigue = 3)

        val firstTurnCreature = FirstTurn().execute(creatures)
        val creatureAction = CreatureAction(actionDie).execute(firstTurnCreature, action, a)
        val nextTurnCreature = nextTurn.execute(creatures, firstTurnCreature.team)

        assertEquals(c, nextTurnCreature)
    }

    @Test
    fun `given all creatures are fatigued, next turn creature becomes null`() {
        val nextTurn = NextTurn()
        val a = Creature(speed = 3, team = 1, dexterity = 3)
        val creatures = listOf(a)
        val action = TestAction(fatigue = 3)

        val firstTurnCreature = FirstTurn().execute(creatures)
        val creatureAction = CreatureAction(actionDie).execute(firstTurnCreature, action, a)
        val nextTurnCreature = nextTurn.execute(creatures, firstTurnCreature.team)

        assertNull(nextTurnCreature)
    }

    @Test
    fun `given all creatures are dead, next turn creature becomes null`() {
        val nextTurn = NextTurn()
        val a = Creature(initialHealth = 0, speed = 3, dexterity = 3)
        val creatures = listOf(a)

        val firstTurnCreature = FirstTurn().execute(creatures)
        val nextTurnCreature = nextTurn.execute(creatures, firstTurnCreature.team)

        assertNull(nextTurnCreature)
    }


}

