package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.creatures.races.SkeletonBehaviour
import pando.domain.Death
import pando.domain.Events
import pando.domain.Rest
import pando.test.CreatureBuilder
import kotlin.test.assertEquals

class SkeletonTest {

    @Test
    fun `given skeleton is dead it should revive in 7 turns`(){
        val events: Events = mock()
        val rest = PublishSubject.create<Rest>()
        val deaths = PublishSubject.create<Death>()
        whenever(events.rest).thenReturn(rest)
        whenever(events.deaths).thenReturn(deaths)
        val creature = CreatureBuilder().build()
        val skeletonBehaviour = SkeletonBehaviour(creature, events)

        deaths.onNext(Death(creature))
        for (x in 1..10) rest.onNext(Rest())

        assertEquals(creature.stats.health, creature.health())
        assertEquals(0, creature.fatigue)
    }
}