package pando.domain

import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.creatures.cards.EyeCard
import pando.creatures.cards.SkeletonCard

class MatchsService(private val matchs: Matchs) {

    private val iaCreatures = {
        mapOf(Position(1, 1) to EyeCard(),
                Position(2, 1) to SkeletonCard(),
                Position(2, 2) to EyeCard())
    }

    fun create(playerCreatures: Map<Position, CreatureCard>): Int {
        val match = Match(playerCreatures, iaCreatures())
        match.start()
        val matchId = matchs.add(match)
        return matchId
    }

    fun get(matchId: Int): Match? {
        return matchs.find(matchId)
    }

}