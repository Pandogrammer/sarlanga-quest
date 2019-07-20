package pando.turns

import pando.actions.FindFastestCreatures
import pando.creatures.Creature

class NextTurn {

    fun execute(creatures: List<Creature>, lastTurnTeam: Int?): Creature? {
        val posibleActiveCreatures = creaturesNeitherFatiguedNorDead(creatures)
        if (posibleActiveCreatures.isEmpty()) return null

        val opposingTeamCreatures = posibleActiveCreatures.filter { it.team != lastTurnTeam }
        if (opposingTeamCreatures.isNotEmpty()) return FindFastestCreatures().execute(opposingTeamCreatures)[0]
        else return FindFastestCreatures().execute(posibleActiveCreatures)[0]
    }

    private fun creaturesNeitherFatiguedNorDead(creatures: List<Creature>): List<Creature> {
        return creatures.filter { it.fatigue == 0  && it.health() != 0}
    }

}