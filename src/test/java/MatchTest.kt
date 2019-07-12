import actions.ActionDie
import actions.Attack
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import creatures.Creature
import creatures.Eye
import creatures.Skeleton
import org.junit.Test
import turns.CreatureAction
import turns.FirstTurn
import turns.NextTurn
import turns.RestingTurn
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MatchTest {

    val actionDie : ActionDie = mock()

    @Test
    fun `match test`(){
        val eye = Eye(team = 1)
        val skeleton = Skeleton(team = 2)
        val creatures = listOf(eye, skeleton)
        var activeCreature : Creature?
        var lastTurnTeam: Int

        activeCreature = FirstTurn().execute(creatures)
        lastTurnTeam = activeCreature.team

        assertEquals(activeCreature, eye)

        roll(9)
        CreatureAction(actionDie).execute(eye, Attack(), skeleton)

        assertEquals(1, eye.fatigue)
        assertEquals(4, skeleton.health)

        activeCreature = NextTurn().execute(creatures, lastTurnTeam)
        lastTurnTeam = activeCreature!!.team

        assertEquals(skeleton, activeCreature)

        roll(2)
        CreatureAction(actionDie).execute(skeleton, Attack(), eye)

        assertEquals(1, skeleton.fatigue)
        assertEquals(4, eye.health)

        activeCreature = NextTurn().execute(creatures, lastTurnTeam)

        assertNull(activeCreature)
        assertEquals(1, eye.fatigue)
        assertEquals(1, skeleton.fatigue)

        RestingTurn().execute(creatures)

        assertEquals(0, eye.fatigue)
        assertEquals(0, skeleton.fatigue)


        activeCreature = NextTurn().execute(creatures, lastTurnTeam)

        assertEquals(eye, activeCreature)

        roll(1)
        CreatureAction(actionDie).execute(eye, Attack(), skeleton)

        assertEquals(3, eye.fatigue)

        activeCreature = NextTurn().execute(creatures, lastTurnTeam)

        assertEquals(skeleton, activeCreature)

        roll(10)
        CreatureAction(actionDie).execute(skeleton, Attack(), eye)

        assertEquals(2, eye.health)
        assertEquals(1, skeleton.fatigue)
    }

    private fun roll(die : Int) {
        whenever(actionDie.roll()).thenReturn(die)
    }
}

