package pando.creatures

class HasBlockers {
    fun execute(objective: Creature, creatures: List<Creature>) : Boolean {
        return creatures.any { isFromSameTeam(it, objective)
                    && isAlive(it)
                    && isBlocking(it, objective)
        }
    }

    private fun isFromSameTeam(it: Creature, objective: Creature) =
            it.team == objective.team

    private fun isAlive(it: Creature) = it.health() > 0

    private fun isBlocking(it: Creature, objective: Creature): Boolean =
            it.position.line == objective.position.line && it.position.column > objective.position.column

}