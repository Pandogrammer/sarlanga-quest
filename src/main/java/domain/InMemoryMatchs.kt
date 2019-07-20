package domain

import creatures.Creature

class InMemoryMatchs : Matchs {

    var lastId = 0
    private val matchs = mutableMapOf<Int, Match>()

    override fun add(match: Match): Int {
        lastId++
        matchs[lastId] = match
        return lastId
    }

    override fun find(matchId: Int): Match? {
        return matchs[matchId]
    }

}