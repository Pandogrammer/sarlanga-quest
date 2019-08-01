package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.ActionDie
import pando.actions.Attack
import pando.domain.Rest
import pando.turns.CreatureAction
import pando.turns.RestingTurn
import kotlin.test.assertEquals

class GolemTest {

    @Test
    fun `given golem has shield and defense, when damaged by 3 it receives 2 less damage`(){
        val restingTurns = PublishSubject.create<Rest>()
        val golem = Golem(restingTurns)
        val initialHealth = golem.health()
        val creature = Creature(initialAttack = 3)
        val action = Attack()
        val actionDie: ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(9)

        CreatureAction(actionDie).execute(creature, action, golem)

        assertEquals(initialHealth - 1 , golem.health())
    }
    
    @Test
    fun `given golem doesnt have shield, when full rested, then shield recovers`(){
        val rest = RestingTurn()
        val restingTurns = rest.executed

        val golem = Golem(restingTurns)
        golem.removeTokens(Token.GOLEM, 1)
        golem.fatigue = 2

        rest.execute(listOf(golem))
        assertEquals(0, golem.getTokens(Token.GOLEM))

        rest.execute(listOf(golem))
        assertEquals(1, golem.getTokens(Token.GOLEM))
    }
}

