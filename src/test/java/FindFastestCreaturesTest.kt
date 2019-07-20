import pando.actions.FindFastestCreatures
import pando.creatures.Creature
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FindFastestCreaturesTest {

    @Test
    fun `one creature has highest speed, then it is selected`(){
        val findFastestsCreatures = FindFastestCreatures()
        val fastestCreature = Creature(speed = 3, dexterity = 3)
        val slowestCreature = Creature(speed = 1, dexterity = 3)
        val creatures = listOf(fastestCreature,  slowestCreature)

        val fastestsCreatures = findFastestsCreatures.execute(creatures)

        assertTrue(fastestsCreatures.contains(fastestCreature))
        assertFalse(fastestsCreatures.contains(slowestCreature))
    }

    @Test
    fun `more than one creature has highest speed, then one of them is selected`(){
        val findFastestsCreatures = FindFastestCreatures()
        val aCreature = Creature(speed = 3, dexterity = 3)
        val bCreature = Creature(speed = 3, dexterity = 3)
        val cCreature = Creature(speed = 1, dexterity = 3)
        val creatures = listOf(aCreature, bCreature, cCreature)

        val fastestsCreatures = findFastestsCreatures.execute(creatures)

        assertTrue(fastestsCreatures.contains(aCreature))
        assertTrue(fastestsCreatures.contains(bCreature))
        assertFalse(fastestsCreatures.contains(cCreature))
    }


}

