package pando.turns

import pando.actions.ActionDie
import pando.test.TestAction
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import pando.test.CreatureBuilder
import kotlin.test.assertEquals

class CreatureActionTest {

    val random : ActionDie = mock()

    @Test
    fun `given creature actions, it gains fatigue`(){
        val creatureAction = ExecuteAction(random)
        val action = TestAction(fatigue = 3)
        val creature = CreatureBuilder().dexterity(3).build()
        val target = CreatureBuilder().dexterity(3).build()

        creatureAction.execute(creature, action, target)

        assertEquals(action.fatigue, creature.fatigue)
    }


}

