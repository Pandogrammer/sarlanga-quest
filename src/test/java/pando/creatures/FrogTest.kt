package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.actions.Action
import pando.actions.ActionDie
import pando.actions.Attack
import pando.creatures.races.FluffyBehaviour
import pando.creatures.races.Frog
import pando.creatures.races.FrogBehaviour
import pando.domain.Damage
import pando.domain.Kill
import pando.test.CreatureBuilder
import pando.turns.CreatureAction
import kotlin.test.assertEquals

class FrogTest {

    @Test
    fun `given frog receives melee damage, the attacker receives one frog poison token`(){
        val events: Events = mock()
        val damage = PublishSubject.create<Damage>()
        whenever(events.damage).thenReturn(damage)
        val creature = CreatureBuilder().build()
        val anotherCreature = CreatureBuilder().build()
        val frogBehaviour = FrogBehaviour(creature, events)
        val action : Action = mock()
        whenever(action.melee).thenReturn(true)

        damage.onNext(Damage(anotherCreature, action, creature))

        assertEquals(1, creature.tokens[Token.FROG])
    }

    @Test
    fun `given frog attacks a target with frog tokens, it deals more damage`(){
        val creature = CreatureBuilder().attack(2).build()
        val anotherCreature = CreatureBuilder().health(5).build()
        anotherCreature.addTokens(Token.FROG, 1)
        val actionDie : ActionDie = mock()
        whenever(actionDie.roll()).thenReturn(9)

        CreatureAction(actionDie).execute(creature, Attack(), anotherCreature)

        assertEquals(1, creature.health())
    }
}

