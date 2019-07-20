package pando.creatures

import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.domain.Kill
import kotlin.test.assertEquals

class FluffyTest {

    @Test
    fun `given fluffy is damaged and kills a creature, it is healed by 1`(){
        val kills = PublishSubject.create<Kill>()
        val fluffy = Fluffy(kills)
        fluffy.damage = 4
        val initialHealth = fluffy.health()

        kills.onNext(Kill(fluffy))

        assertEquals(initialHealth + 1, fluffy.health())
    }

    @Test
    fun `given fluffy is damaged and has already killed a creature, when it kills another, it is healed by 3 (1+2)`(){
        val kills = PublishSubject.create<Kill>()
        val fluffy = Fluffy(kills)
        fluffy.damage = 4
        val initialHealth = fluffy.health()

        kills.onNext(Kill(fluffy))
        kills.onNext(Kill(fluffy))

        assertEquals(initialHealth + 3, fluffy.health())
    }

    @Test
    fun `given fluffy is full health, when it kills a creature, it is not healed but the counter increments`(){
        val kills = PublishSubject.create<Kill>()
        val fluffy = Fluffy(kills)
        val initialHealth = fluffy.health()

        kills.onNext(Kill(fluffy))

        assertEquals(initialHealth, fluffy.health())
        assertEquals(1, fluffy.killCounter)
    }
}

