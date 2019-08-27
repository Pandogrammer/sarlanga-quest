package pando.turns

import pando.creatures.SpawnedCreature

class WinnerValidation {
    fun execute(spawnedCreatures: List<SpawnedCreature>): Int? {
        if (allAlive(spawnedCreatures)) return null

        if (allDeadFromTeamOne(spawnedCreatures)) return 2
        if (allDeadFromTeamTwo(spawnedCreatures)) return 1

        return null
    }

    private fun allDeadFromTeamTwo(spawnedCreatures: List<SpawnedCreature>) =
            spawnedCreatures.filter { it.team == 2 }.all { it.health() == 0 }

    private fun allDeadFromTeamOne(spawnedCreatures: List<SpawnedCreature>) =
            spawnedCreatures.filter { it.team == 1 }.all { it.health() == 0 }

    private fun allAlive(spawnedCreatures: List<SpawnedCreature>): Boolean {
        return spawnedCreatures.none { it.health() == 0 }
    }

}
