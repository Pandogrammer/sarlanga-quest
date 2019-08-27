package pando.turns

import pando.actions.FindFastestCreatures
import pando.creatures.SpawnedCreature

class NextTurn {

    fun execute(spawnedCreatures: List<SpawnedCreature>, lastTurnTeam: Int?): SpawnedCreature? {
        val posibleActiveCreatures = creaturesNeitherFatiguedNorDead(spawnedCreatures)
        if (posibleActiveCreatures.isEmpty()) return null

        val opposingTeamCreatures = posibleActiveCreatures.filter { it.team != lastTurnTeam }
        if (opposingTeamCreatures.isNotEmpty()) return FindFastestCreatures().execute(opposingTeamCreatures)[0]
        else return FindFastestCreatures().execute(posibleActiveCreatures)[0]
    }

    private fun creaturesNeitherFatiguedNorDead(spawnedCreatures: List<SpawnedCreature>): List<SpawnedCreature> {
        return spawnedCreatures.filter { it.fatigue == 0  && it.health() != 0}
    }

}