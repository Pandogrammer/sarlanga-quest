package pando.creatures

import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.domain.Death
import kotlin.test.assertEquals

class SkeletonTest {

    @Test
    fun `given skeleton is dead it should revive in 7 turns`(){
        val restingTurns = PublishSubject.create<Unit>()
        val deaths = PublishSubject.create<Death>()
        val skeleton = Skeleton(1, restingTurns, deaths)

        deaths.onNext(Death(skeleton))
        for (x in 1..10) restingTurns.onNext(Unit)

        assertEquals(skeleton.initialHealth, skeleton.health())
        assertEquals(0, skeleton.fatigue)
    }
}