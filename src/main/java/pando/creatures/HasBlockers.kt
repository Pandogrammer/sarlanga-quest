package pando.creatures

class HasBlockers {
    fun execute(objective: SpawnedCreature, spawnedCreatures: List<SpawnedCreature>) : Boolean {
        return spawnedCreatures.any { isFromSameTeam(it, objective)
                    && isAlive(it)
                    && isBlocking(it, objective)
        }
    }

    private fun isFromSameTeam(it: SpawnedCreature, objective: SpawnedCreature) =
            it.team == objective.team

    private fun isAlive(it: SpawnedCreature) = it.health() > 0

    private fun isBlocking(it: SpawnedCreature, objective: SpawnedCreature): Boolean =
            it.position.line == objective.position.line && it.position.column > objective.position.column

}