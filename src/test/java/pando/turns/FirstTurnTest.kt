package pando.turns

import pando.creatures.Creature
import pando.creatures.Position
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirstTurnTest {

    @Test
    fun `given one fastest creature, it becomes active`(){
        val firstTurn = FirstTurn()
        val slowestCreature = Creature(speed = 1, dexterity = 3)
        val fastestCreature = Creature(speed = 3, dexterity = 3)
        val creatureList = listOf(slowestCreature, fastestCreature)

        val activeCreature = firstTurn.execute(creatureList)

        assertEquals(fastestCreature, activeCreature)
    }

    @Test
    fun `given more than one fastest creature in same team, it is decided by position`(){
        val firstTurn = FirstTurn()
        val aCreature = Creature(speed = 3, position = Position(1, 2), dexterity = 3)
        val bCreature = Creature(speed = 3, position = Position(1, 1), dexterity = 3)

        val activeCreature = firstTurn.execute(listOf(aCreature, bCreature))

        assertEquals(bCreature, activeCreature)
    }

    @Test
    fun `given more than one fastest creature in each team, it is decided by random`(){
        val firstTurn = FirstTurn()
        val aCreature = Creature(speed = 3, team = 1, dexterity = 3)
        val bCreature = Creature(speed = 2, team = 1, dexterity = 3)
        val cCreature = Creature(speed = 3, team = 2, dexterity = 3)
        val dCreature = Creature(speed = 2, team = 2, dexterity = 3)

        val activeCreature = firstTurn.execute(listOf(aCreature,bCreature,cCreature,dCreature))

        assertTrue(activeCreature == aCreature || activeCreature == cCreature)
    }

}

