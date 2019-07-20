package pando.creatures

import io.reactivex.subjects.PublishSubject
import org.junit.Test
import pando.domain.Kill
import kotlin.test.assertEquals

class EyeTest {

    @Test
    fun `given eye kills a creature, attack is boosted`(){
        val deaths = PublishSubject.create<Kill>()
        val eye = Eye(1, deaths)
        val initialAttack = eye.attack

        deaths.onNext(Kill(eye))

        assertEquals(initialAttack + 1, eye.attack)
    }
}