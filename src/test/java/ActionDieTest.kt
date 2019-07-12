import actions.ActionDie
import actions.Attack
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import creatures.Creature
import org.junit.Test
import turns.CreatureAction
import kotlin.test.assertEquals

class ActionDieTest {

    @Test
    fun `given action succeeded, action is executed`(){
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(6)
        val a = Creature(dexterity = 3, attack = 2)
        val b = Creature(initialHealth = 4)

        CreatureAction(actionDie).execute(a, Attack(), b)

        assertEquals(1, a.fatigue)
        assertEquals(2, b.health)
    }


    @Test
    fun `given action didn't succeed, action is not executed`(){
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(2)
        val a = Creature(dexterity = 3, attack = 2)
        val b = Creature(initialHealth = 4)

        CreatureAction(actionDie).execute(a, Attack(), b)

        assertEquals(1, a.fatigue)
        assertEquals(4, b.health)
    }


    @Test
    fun `given action critical, damage is doubled`(){
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(10)
        val a = Creature(dexterity = 3, attack = 2)
        val b = Creature(initialHealth = 4)

        CreatureAction(actionDie).execute(a, Attack(), b)

        assertEquals(1, a.fatigue)
        assertEquals(0, b.health)
    }


    @Test
    fun `given action critically failed, action is not executed, and actor is fully fatigued`(){
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(1)
        val a = Creature(dexterity = 3, attack = 2)
        val b = Creature(initialHealth = 4)

        CreatureAction(actionDie).execute(a, Attack(), b)

        assertEquals(3, a.fatigue)
        assertEquals(4, b.health)
    }


}



