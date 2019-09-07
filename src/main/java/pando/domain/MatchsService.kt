package pando.domain

import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.creatures.cards.EyeCard
import pando.creatures.cards.SkeletonCard
import java.util.*

class MatchsService(private val matchs: Matchs) {

    //caca
    private val account_match = HashMap<String, Int>()

    private val iaCreatures = {
        mapOf(Position(1, 1) to EyeCard(),
                Position(2, 1) to SkeletonCard(),
                Position(2, 2) to EyeCard())
    }

    fun create(playerCreatures: Map<Position, CreatureCard>, accountId: String): Int {
        val match = Match(playerCreatures, iaCreatures())
        match.start()
        val matchId = matchs.add(match)
        account_match[accountId] = matchId
        return matchId
    }

    fun get(matchId: Int): Match? {
        return matchs.find(matchId)
    }

    fun get(accountId: String): Match?{
        account_match[accountId]?.let{ return matchs.find(it) }
        return null
    }

    fun isInMatch(accountId: String): Boolean {
        return account_match.containsKey(accountId)
    }
}