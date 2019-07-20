package pando.turns

import pando.creatures.Creature

class WinnerValidation {
    fun execute(creatures: List<Creature>): Int? {
        if (allAlive(creatures)) return null

        if (allDeadFromTeamOne(creatures)) return 2
        if (allDeadFromTeamTwo(creatures)) return 1

        return null
    }

    private fun allDeadFromTeamTwo(creatures: List<Creature>) =
            creatures.filter { it.team == 2 }.all { it.initialHealth == 0 }

    private fun allDeadFromTeamOne(creatures: List<Creature>) =
            creatures.filter { it.team == 1 }.all { it.initialHealth == 0 }

    private fun allAlive(creatures: List<Creature>): Boolean {
        return creatures.none { it.initialHealth == 0 }
    }

}
