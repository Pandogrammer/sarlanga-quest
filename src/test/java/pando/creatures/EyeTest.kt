package pando.creatures

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.creatures.races.EyeBehaviour
import pando.domain.Events
import pando.domain.Kill
import pando.test.CreatureBuilder
import kotlin.test.assertEquals

class EyeTest {

    @Test
    fun `given eye kills a creature, attack is boosted`(){
        val events: Events = mock()
        val kills = PublishSubject.create<Kill>()
        whenever(events.kills).thenReturn(kills)
        val creature = CreatureBuilder().build()
        val eyeBehaviour = EyeBehaviour().attachTo(creature, events)
        val initialAttack = creature.attack()

        kills.onNext(Kill(creature))

        assertEquals(initialAttack + 1, creature.attack())
    }
}