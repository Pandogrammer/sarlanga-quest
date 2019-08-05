package pando

import org.junit.Test
import pando.creatures.HasBlockers
import pando.creatures.Position
import pando.test.CreatureBuilder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HasBlockersTest {

    @Test
    fun `given a creature is in front of another, then it has blockers`(){
        val hasBlockers = HasBlockers()
        val attacker = CreatureBuilder().build()
        val objective = CreatureBuilder().position(Position(1,1)).build()
        val defender = CreatureBuilder().position(Position(1,2)).build()

        val result = hasBlockers.execute(objective, listOf(attacker, objective, defender))

        assertTrue(result)
    }


    @Test
    fun `given a creature is in front of another but is dead, then it doesnt have blockers`(){
        val hasBlockers = HasBlockers()
        val attacker = CreatureBuilder().build()
        val objective = CreatureBuilder().position(Position(1,1)).build()
        val defender = CreatureBuilder().position(Position(1,2)).health(0).build()

        val result = hasBlockers.execute(objective, listOf(attacker, objective, defender))

        assertFalse(result)
    }


}

