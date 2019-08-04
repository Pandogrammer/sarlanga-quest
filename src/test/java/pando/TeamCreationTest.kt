package pando

import org.junit.Test
import pando.creatures.Creature
import pando.creatures.Position
import pando.domain.Team
import kotlin.test.*

class TeamCreationTest {

    @Test
    fun `given team is empty, when add creature, then creature is added`(){
        val creature = Creature()
        val position = Position(1,1)
        val team = Team()

        team.addCreature(creature, position)

        assertTrue(team.creatures.containsValue(creature))
        assertEquals(creature, team.getCreature(position))
    }

    @Test
    fun `given position is already occupied, when add creature, then creature is not added`(){
        val creature = Creature()
        val notAddedCreature = Creature()
        val position = Position(1,1)
        val team = Team()

        team.addCreature(creature, position)
        team.addCreature(notAddedCreature, position)

        assertFalse(team.creatures.containsValue(notAddedCreature))
        assertNotEquals(notAddedCreature, team.getCreature(position))
    }

    @Test
    fun `given team essence is 1, when creature of essence 1 is added, then it is added`(){
        val team = Team(essence = 1)
        val creature = Creature(essence = 1)
        val position = Position(1,1)

        team.addCreature(creature, position)

        assertTrue(team.creatures.containsValue(creature))
        assertEquals(creature, team.getCreature(position))
    }

    @Test
    fun `given team essence is 1, when creature of essence 2 is added, then it is not added`(){
        val team = Team(essence = 1)
        val creature = Creature(essence = 2)
        val position = Position(1,1)

        team.addCreature(creature, position)

        assertFalse(team.creatures.containsValue(creature))
    }

    @Test
    fun `given position is invalid, when add creature, then it is not added`(){
        val team = Team()
        val creature = Creature()
        val position = Position(5,5)

        team.addCreature(creature, position)

        assertFalse(team.creatures.containsValue(creature))
    }

    @Test
    fun `given team has creature, when removed by position, then position is empty`(){
        val team = Team()
        val creature = Creature()
        val position = Position(1, 1)
        team.addCreature(creature, position)

        team.removeCreature(position)

        assertNull(team.getCreature(position))
    }

    
}

