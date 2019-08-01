package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.domain.Damage
import pando.turns.CreatureAction
import kotlin.test.assertEquals

class FrogTest {

    @Test
    fun `given frog receives melee damage, the attacker receives one frog poison token`(){
        val damage = PublishSubject.create<Damage>()
        val frog = Frog(damage)
        val creature = Creature()
        val action : Action = mock()
        whenever(action.melee).thenReturn(true)

        damage.onNext(Damage(creature, action, frog))

        assertEquals(1, creature.tokens[Token.FROG])
    }

    @Test
    fun `given frog attacks a target with frog tokens, it deals more damage`(){
        val damage = PublishSubject.create<Damage>()
        val frog = Frog(damage)
        val creature = Creature(5)
        creature.addTokens(Token.FROG, 1)
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(9)

        CreatureAction(actionDie).execute(frog, Attack(), creature)

        assertEquals(1, creature.health())
    }
}

