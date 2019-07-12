package turns

import actions.FindFastestCreatures
import creatures.Creature
import kotlin.random.Random

class FirstTurn {

    fun execute(creatures: List<Creature>): Creature {
        val fastestCreatures = FindFastestCreatures().execute(creatures)
        var teamCreatures : List<Creature>

        if (bothTeamHasCreatures(fastestCreatures)) {
            teamCreatures = selectCreaturesFromARandomTeam(fastestCreatures)
        } else teamCreatures = fastestCreatures

        return selectFirstCreatureByPosition(teamCreatures)
    }

    private fun selectFirstCreatureByPosition(fastestCreatures: List<Creature>) =
            fastestCreatures.sortedBy { it.position }[0]

    private fun selectCreaturesFromARandomTeam(creatures: List<Creature>): List<Creature> {
        val team = Random.nextInt(1, 3)
        return selectCreaturesFromTeam(creatures, team)
    }

    private fun selectCreaturesFromTeam(creatures: List<Creature>, team: Int) =
            creatures.filter { it.team == team }


    private fun bothTeamHasCreatures(fastestCreatures: List<Creature>): Boolean {
        return fastestCreatures.map { it.team }.distinct().count() > 1
    }
}