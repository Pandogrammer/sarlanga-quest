import pando.actions.FindFastestCreatures
import org.junit.Test
import pando.test.CreatureBuilder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FindFastestCreaturesTest {

    @Test
    fun `one creature has highest speed, then it is selected`(){
        val findFastestsCreatures = FindFastestCreatures()
        val fastestCreature = CreatureBuilder().speed(3).build()
        val slowestCreature = CreatureBuilder().speed(1).build()
        val creatures = listOf(fastestCreature,  slowestCreature)

        val fastestsCreatures = findFastestsCreatures.execute(creatures)

        assertTrue(fastestsCreatures.contains(fastestCreature))
        assertFalse(fastestsCreatures.contains(slowestCreature))
    }

    @Test
    fun `more than one creature has highest speed, then one of them is selected`(){
        val findFastestsCreatures = FindFastestCreatures()
        val aCreature = CreatureBuilder().speed(3).build()
        val bCreature = CreatureBuilder().speed(3).build()
        val cCreature = CreatureBuilder().speed(1).build()
        val creatures = listOf(aCreature, bCreature, cCreature)

        val fastestsCreatures = findFastestsCreatures.execute(creatures)

        assertTrue(fastestsCreatures.contains(aCreature))
        assertTrue(fastestsCreatures.contains(bCreature))
        assertFalse(fastestsCreatures.contains(cCreature))
    }


}

