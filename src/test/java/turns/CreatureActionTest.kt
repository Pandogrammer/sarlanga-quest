package turns

import actions.ActionDie
import actions.TestAction
import com.nhaarman.mockitokotlin2.mock
import creatures.Creature
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class CreatureActionTest {

    val random : ActionDie = mock()

    @Test
    fun `given creature actions, it gains fatigue`(){
        val creatureAction = CreatureAction(random)
        val action = TestAction(fatigue = 3)
        val creature = Creature(dexterity = 3)
        val target = Creature(dexterity = 3)

        creatureAction.execute(creature, action, target)

        assertEquals(action.fatigue, creature.fatigue)
    }


}

