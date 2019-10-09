package pando.domain

import io.reactivex.subjects.PublishSubject
import pando.creatures.Position
import pando.creatures.cards.*
import kotlin.random.Random

class MatchsService(private val matchs: Matchs) {
    val matchCreated = PublishSubject.create<MatchCreation>()

    fun create(playerCreatures: Map<Position, CreatureCard>, accountId: String): Int {
        val match = Match(playerCreatures, generateIaCreatures())
        val matchId = matchs.add(match)
        matchCreated.onNext(MatchCreation(matchId, accountId))
        match.start()
        return matchId
    }

    private fun generateIaCreatures(): Map<Position, CreatureCard> {
        return mapOf(Position(1, 1) to getRandomCreature(),
                Position(2, 1) to getRandomCreature(),
                Position(1, 2) to getRandomCreature(),
                Position(2, 2) to getRandomCreature())
    }

    private fun getRandomCreature(): CreatureCard {
        return when (Random.nextInt(1, 4)) {
            1 -> EyeCard()
            2 -> SkeletonCard()
            3 -> FluffyCard()
            4 -> FrogCard()
            else -> EyeCard()
        }
    }

    fun get(matchId: Int): Match? {
        return matchs.find(matchId)
    }

}

class MatchCreation(val matchId: Int, val accountId: String)
