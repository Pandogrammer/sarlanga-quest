package pando.turns

import pando.actions.FindFastestCreatures
import pando.creatures.SpawnedCreature
import kotlin.random.Random

class FirstTurn {

    fun execute(spawnedCreatures: List<SpawnedCreature>): SpawnedCreature {
        val fastestCreatures = FindFastestCreatures().execute(spawnedCreatures)
        var teamSpawnedCreatures : List<SpawnedCreature>

        if (bothTeamHasCreatures(fastestCreatures)) {
            teamSpawnedCreatures = selectCreaturesFromARandomTeam(fastestCreatures)
        } else teamSpawnedCreatures = fastestCreatures

        return selectFirstCreatureByPosition(teamSpawnedCreatures)
    }

    private fun selectFirstCreatureByPosition(fastestSpawnedCreatures: List<SpawnedCreature>) =
            fastestSpawnedCreatures.sortedBy { it.position }[0]

    private fun selectCreaturesFromARandomTeam(spawnedCreatures: List<SpawnedCreature>): List<SpawnedCreature> {
        val team = Random.nextInt(1, 3)
        return selectCreaturesFromTeam(spawnedCreatures, team)
    }

    private fun selectCreaturesFromTeam(spawnedCreatures: List<SpawnedCreature>, team: Int) =
            spawnedCreatures.filter { it.team == team }


    private fun bothTeamHasCreatures(fastestSpawnedCreatures: List<SpawnedCreature>): Boolean {
        return fastestSpawnedCreatures.map { it.team }.distinct().count() > 1
    }
}