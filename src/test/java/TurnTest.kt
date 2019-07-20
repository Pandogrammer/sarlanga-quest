import actions.ActionDie
import actions.Attack
import creatures.Creature
import domain.Match
import org.junit.Test
import turns.CreatureAction

class TurnTest {

    @Test
    fun `given action then turn has action`(){
        val creatures = listOf(Creature(speed = 2, team = 1), Creature(speed = 3, team = 2))
        val match = Match(creatures)
        match.turns.subscribe{ println("Speed: ${it.creature.speed} - Team: ${it.creature.team}") }
        match.creatureAction(0)
        match.creatureAction(0)
        match.creatureAction(0)
        match.creatureAction(0)
        match.creatureAction(0)
        match.creatureAction(0)
        match.creatureAction(0)
    }
}