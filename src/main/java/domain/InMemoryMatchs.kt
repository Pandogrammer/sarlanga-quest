package domain

class InMemoryMatchs : Matchs {

    var lastId = 1
    private val matchs = mutableMapOf<Int, Match>()

    override fun add(match: Match): Int {
        val matchId = lastId
        lastId++
        matchs[matchId] = match
        return matchId
    }

    override fun find(matchId: Int): Match? {
        return matchs[matchId]
    }

}

