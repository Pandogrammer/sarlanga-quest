package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.races.FrogBehaviour
import pando.domain.DamageEvent
import pando.domain.Events
import pando.test.CreatureBuilder
import pando.turns.CreatureAction
import kotlin.test.assertEquals

class FrogTest {

    @Test
    fun `given frog receives melee damage, the attacker receives one frog poison token`(){
        val events: Events = mock()
        val damage = PublishSubject.create<DamageEvent>()
        whenever(events.damageEvent).thenReturn(damage)
        val frog = CreatureBuilder().build()
        val anotherCreature = CreatureBuilder().build()
        val frogBehaviour = FrogBehaviour(frog, events)
        val action : Action = mock()
        whenever(action.melee).thenReturn(true)

        damage.onNext(DamageEvent(anotherCreature, action, frog))

        assertEquals(1, anotherCreature.tokens[Token.FROG])
    }

    @Test
    fun `given frog attacks a target with frog tokens, it deals more damage and tokens are consumed`(){
        val events: Events = mock()
        val damage = PublishSubject.create<DamageEvent>()
        whenever(events.damageEvent).thenReturn(damage)
        val frog = CreatureBuilder().build()
        val frogBehaviour = FrogBehaviour(frog, events)
        val anotherCreature = CreatureBuilder().health(4).build()
        anotherCreature.addTokens(Token.FROG, 2)

        damage.onNext(DamageEvent(frog, Attack(), anotherCreature))

        assertEquals(2, anotherCreature.health())
        assertEquals(0, anotherCreature.getTokens(Token.FROG))
    }
}

