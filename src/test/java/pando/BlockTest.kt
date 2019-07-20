package pando

import org.junit.Test
import pando.creatures.Creature
import pando.creatures.Position
import pando.domain.Match
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

class HasBlockers {
    fun execute(objective: Creature, creatures: List<Creature>) : Boolean {
        return creatures.any { isFromSameTeam(it, objective)
                    && isAlive(it)
                    && isBlocking(it, objective)
        }
    }

    private fun isFromSameTeam(it: Creature, objective: Creature) =
            it.team == objective.team

    private fun isAlive(it: Creature) = it.health() > 0

    private fun isBlocking(it: Creature, objective: Creature): Boolean =
            it.position.line == objective.position.line && it.position.column > objective.position.column

}
