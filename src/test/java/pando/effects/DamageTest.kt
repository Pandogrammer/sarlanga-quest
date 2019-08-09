package pando.effects

import org.junit.Test
import pando.test.CreatureBuilder
import kotlin.test.assertEquals

class DamageTest {

    @Test
    fun `given creature receives damage, health is decreased`(){
        val damage = Damage(1)
        val creature = CreatureBuilder().health(5).build()

        damage.applyOn(creature)

        assertEquals(4, creature.health())
    }
}

