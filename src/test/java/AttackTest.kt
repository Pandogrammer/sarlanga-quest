import actions.Attack
import creatures.Creature
import org.junit.Assert
import org.junit.Test

class AttackTest {

    @Test
    fun `a creature with 2 attack attacks another, then health decrements 2 points`(){
        val attack = Attack()
        val attackValue = 2
        val aCreature = Creature(attack = attackValue, speed = 10, dexterity = 3)
        val anotherCreature = Creature(initialHealth = 10, speed = 10, dexterity = 3)
        val updatedHealth = anotherCreature.health - attackValue

        attack.execute(aCreature, anotherCreature, false)

        Assert.assertEquals(updatedHealth, anotherCreature.health)
    }

    @Test
    fun `a creature with 2 attack attacks another, objective has 1 defense, then health decrements 1 point`(){
        val attack = Attack()
        val attackValue = 2
        val aCreature = Creature(attack = attackValue, speed = 10, dexterity = 3)

        val defenseValue = 1
        val anotherCreature = Creature(initialHealth = 10, defense = defenseValue, speed = 10, dexterity = 3)
        val updatedHealth = anotherCreature.health - (attackValue - defenseValue)

        attack.execute(aCreature, anotherCreature, false)

        Assert.assertEquals(updatedHealth, anotherCreature.health)
    }

    @Test
    fun `a creature with 1 health is attacked with 2, then health is 0`(){
        val attack = Attack()
        val attacker = Creature(attack = 2, speed = 10, dexterity = 3)
        val objective = Creature(initialHealth = 1, speed = 10, dexterity = 3)

        attack.execute(attacker, objective, false)

        Assert.assertEquals(0, objective.health)
    }

}

