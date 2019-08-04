package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.races.EyeBehaviour
import pando.creatures.races.Golem
import pando.creatures.races.GolemBehaviour
import pando.domain.Kill
import pando.domain.Rest
import pando.test.CreatureBuilder
import pando.turns.CreatureAction
import pando.turns.RestingTurn
import kotlin.test.assertEquals

class GolemTest {

    @Test
    fun `given golem has shield and defense, when damaged it receives 1 less damage`(){
        val events: Events = mock()
        val rest = PublishSubject.create<Rest>()
        whenever(events.rest).thenReturn(rest)
        val creature = CreatureBuilder().health(6).build()
        val attacker = CreatureBuilder().attack(3).build()
        val golemBehaviour = GolemBehaviour(creature, events)
        val action = Attack()
        val actionDie: ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(9)
        val initialHealth = creature.health()

        CreatureAction(actionDie).execute(attacker, action, creature)

        assertEquals(initialHealth - (attacker.attack() - 1), creature.health())
    }
    
    @Test
    fun `given golem doesnt have shield, when full rested, then shield recovers`(){
        val rest = RestingTurn()
        val restingTurns = rest.executed
        val events: Events = mock()
        whenever(events.rest).thenReturn(restingTurns)
        val creature = CreatureBuilder().build()
        val golemBehaviour = GolemBehaviour(creature, events)

        creature.removeTokens(Token.GOLEM, 1)
        creature.fatigue = 2

        rest.execute(listOf(creature))
        assertEquals(0, creature.getTokens(Token.GOLEM))

        rest.execute(listOf(creature))
        assertEquals(1, creature.getTokens(Token.GOLEM))
    }
}

