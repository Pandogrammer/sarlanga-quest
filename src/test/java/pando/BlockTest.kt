package pando

import org.junit.Test
import pando.creatures.HasBlockers
import pando.creatures.Creature
import pando.creatures.Position
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockTest {

    @Test
    fun `given a creature is in front of another, then it has blockers`(){
        val hasBlockers = HasBlockers()
        val attacker = Creature()
        val objective = Creature(position = Position(1, 1))
        val defender = Creature(position = Position(1, 2))

        val result = hasBlockers.execute(objective, listOf(attacker, objective, defender))

        assertTrue(result)
    }


    @Test
    fun `given a creature is in front of another but is dead, then it doesnt have blockers`(){
        val hasBlockers = HasBlockers()
        val attacker = Creature()
        val objective = Creature(position = Position(1, 1))
        val defender = Creature(initialHealth = 0, position = Position(1, 2))

        val result = hasBlockers.execute(objective, listOf(attacker, objective, defender))

        assertFalse(result)
    }


}

