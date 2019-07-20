package pando.domain

import pando.creatures.CreatureCode

class MatchsService(private val matchs: Matchs) {

    fun create(playerCreatures: List<CreatureCode>, iaCreatures: List<CreatureCode>): Int {
        val match = Match(playerCreatures, iaCreatures)
        val matchId = matchs.add(match)
        return matchId
    }

}