package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.races.GolemBehaviour
import pando.domain.DamageEvent
import pando.domain.Events
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
        val damageEvents = PublishSubject.create<DamageEvent>()
        whenever(events.rest).thenReturn(rest)
        whenever(events.damageEvent).thenReturn(damageEvents)
        val golem = CreatureBuilder().health(6).build()
        val attacker = CreatureBuilder().attack(3).build()
        val golemBehaviour = GolemBehaviour(golem, events)
        val action = Attack()
        val actionDie: ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(9)
        val initialHealth = golem.health()

        CreatureAction(actionDie).execute(attacker, action, golem)
        damageEvents.onNext(DamageEvent(attacker, action, golem))

        assertEquals(initialHealth - (attacker.attack() - 1), golem.health())
    }
    
    @Test
    fun `given golem doesnt have shield, when full rested, then shield recovers`(){
        val restingTurn = RestingTurn()
        val rest = restingTurn.executed
        val events: Events = mock()
        val damageEvents = PublishSubject.create<DamageEvent>()
        whenever(events.rest).thenReturn(rest)
        whenever(events.damageEvent).thenReturn(damageEvents)
        val golem = CreatureBuilder().speed(1).build()
        val golemBehaviour = GolemBehaviour(golem, events)

        golem.removeTokens(Token.GOLEM, 1)
        golem.fatigue = 2

        restingTurn.execute(listOf(golem))
        assertEquals(0, golem.getTokens(Token.GOLEM))

        restingTurn.execute(listOf(golem))
        assertEquals(1, golem.getTokens(Token.GOLEM))
    }
}

