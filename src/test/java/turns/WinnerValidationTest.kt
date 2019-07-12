package turns

import creatures.Creature
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WinnerValidationTest {

    @Test
    fun `given all creatures of one team are dead, other team wins`(){
        val winnerValidation = WinnerValidation()
        val creatures = listOf(Creature(initialHealth = 0, team = 1, dexterity = 3), Creature(initialHealth = 1, team = 2, dexterity = 3))

        val winningTeam = winnerValidation.execute(creatures)

        assertEquals(2, winningTeam)
    }


    @Test
    fun `given all creatures are alive, there should not be a winner`(){
        val winnerValidation = WinnerValidation()
        val creatures = listOf(Creature(initialHealth = 1, team = 1, dexterity = 3), Creature(initialHealth = 1, team = 2, dexterity = 3))

        val winningTeam = winnerValidation.execute(creatures)

        assertNull(winningTeam)
    }
}