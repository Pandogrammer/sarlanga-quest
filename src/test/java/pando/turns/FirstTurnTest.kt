package pando.turns

import pando.creatures.Position
import org.junit.Test
import pando.test.CreatureBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirstTurnTest {

    @Test
    fun `given one fastest creature, it becomes active`(){
        val firstTurn = FirstTurn()
        val slowestCreature = CreatureBuilder().dexterity(3).speed(1).build()
        val fastestCreature = CreatureBuilder().dexterity(3).speed(3).build()
        val creatureList = listOf(slowestCreature, fastestCreature)

        val activeCreature = firstTurn.execute(creatureList)

        assertEquals(fastestCreature, activeCreature)
    }

    @Test
    fun `given more than one fastest creature in same team, it is decided by position`(){
        val firstTurn = FirstTurn()
        val aCreature = CreatureBuilder().dexterity(3).speed(3).position(Position(1, 2)).build()
        val bCreature = CreatureBuilder().dexterity(3).speed(3).position(Position(1, 1)).build()

        val activeCreature = firstTurn.execute(listOf(aCreature, bCreature))

        assertEquals(bCreature, activeCreature)
    }

    @Test
    fun `given more than one fastest creature in each team, it is decided by random`(){
        val firstTurn = FirstTurn()
        val aCreature = CreatureBuilder().speed(3).dexterity(3).team(1).build()
        val bCreature = CreatureBuilder().speed(2).dexterity(3).team(1).build()
        val cCreature = CreatureBuilder().speed(3).dexterity(3).team(2).build()
        val dCreature = CreatureBuilder().speed(2).dexterity(3).team(2).build()

        val activeCreature = firstTurn.execute(listOf(aCreature,bCreature,cCreature,dCreature))

        assertTrue(activeCreature == aCreature || activeCreature == cCreature)
    }

}

