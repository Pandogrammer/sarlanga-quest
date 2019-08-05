package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.creatures.races.FluffyBehaviour
import pando.domain.Events
import pando.domain.Kill
import pando.test.CreatureBuilder
import kotlin.test.assertEquals

class FluffyTest {

    @Test
    fun `given fluffy is damaged and kills a creature, it is healed by 1`(){
        val events: Events = mock()
        val kills = PublishSubject.create<Kill>()
        whenever(events.kills).thenReturn(kills)
        val creature = CreatureBuilder().build()
        val fluffyBehaviour = FluffyBehaviour(creature, events)
        creature.damageCounters = 4
        val initialHealth = creature.health()

        kills.onNext(Kill(creature))

        assertEquals(initialHealth + 1, creature.health())
    }

    @Test
    fun `given fluffy is damaged and has already killed a creature, when it kills another, it is healed by 3 (1+2)`(){
        val events: Events = mock()
        val kills = PublishSubject.create<Kill>()
        whenever(events.kills).thenReturn(kills)
        val creature = CreatureBuilder().build()
        val fluffyBehaviour = FluffyBehaviour(creature, events)
        creature.damageCounters = 4
        val initialHealth = creature.health()

        kills.onNext(Kill(creature))
        kills.onNext(Kill(creature))

        assertEquals(initialHealth + 3, creature.health())
    }

    @Test
    fun `given fluffy is full health, when it kills a creature, it is not healed but the counter increments`(){
        val events: Events = mock()
        val kills = PublishSubject.create<Kill>()
        whenever(events.kills).thenReturn(kills)
        val creature = CreatureBuilder().build()
        val fluffyBehaviour = FluffyBehaviour(creature, events)
        val initialHealth = creature.health()

        kills.onNext(Kill(creature))

        assertEquals(initialHealth, creature.health())
        assertEquals(1, fluffyBehaviour.killCounter)
    }
}
