package pando.domain

import io.reactivex.subjects.PublishSubject
import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.creatures.cards.FluffyCard
import pando.creatures.cards.SkeletonCard

class MatchsService(private val matchs: Matchs) {
    val matchCreated = PublishSubject.create<MatchCreation>()

    private val iaCreatures = {
        mapOf(Position(1, 1) to SkeletonCard(),
                Position(2, 1) to FluffyCard())
    }

    fun create(playerCreatures: Map<Position, CreatureCard>, accountId: String): Int {
        val match = Match(playerCreatures, iaCreatures())
        val matchId = matchs.add(match)
        matchCreated.onNext(MatchCreation(matchId, accountId))
        match.start()
        return matchId
    }

    fun get(matchId: Int): Match? {
        return matchs.find(matchId)
    }

}

class MatchCreation(val matchId: Int, val accountId: String)
