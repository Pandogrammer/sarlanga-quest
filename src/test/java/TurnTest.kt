import org.junit.Test
import pando.creatures.CreatureCode
import pando.domain.Match

class TurnTest {

    @Test
    fun `given action then turn has action`(){
        val match = Match(listOf(CreatureCode.EYE), listOf(CreatureCode.SKELETON, CreatureCode.SKELETON, CreatureCode.SKELETON))
        match.actions.subscribe{
            println("Creature: ${it.creature} - Attack roll: ${it.roll}")
        }

        match.deaths.subscribe{
            println("${it.killed} was killed by ${it.killer}")
            println("Eye attack: ${match.creatures.get(0).attack}")
        }

        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
        match.creatureAction(1)
    }

}