package domain

import creatures.Creature

class MatchsService(private val matchs: Matchs) {

    fun create(creatures: List<Creature>): Int {
        val match = Match(creatures)
        val matchId = matchs.add(match)
        return matchId
    }

}