package pando.turns

import org.junit.Test
import pando.test.CreatureBuilder
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WinnerValidationTest {

    @Test
    fun `given all creatures of one team are dead, other team wins`(){
        val winnerValidation = WinnerValidation()
        val creatures = listOf(
                CreatureBuilder().team(1).health(0).build(),
                CreatureBuilder().team(2).health(1).build()
        )

        val winningTeam = winnerValidation.execute(creatures)

        assertEquals(2, winningTeam)
    }


    @Test
    fun `given all creatures are alive, there should not be a winner`(){
        val winnerValidation = WinnerValidation()
        val creatures = listOf(
                CreatureBuilder().team(1).health(1).build(),
                CreatureBuilder().team(2).health(1).build()
        )

        val winningTeam = winnerValidation.execute(creatures)

        assertNull(winningTeam)
    }
}