package pando.domain

import pando.creatures.CreatureCode
import pando.creatures.Position

class MatchsService(private val matchs: Matchs) {

    val iaCreatures = {
        mapOf(  Position(1,1) to CreatureCode.EYE,
                Position(2, 1) to CreatureCode.SKELETON,
                Position(2, 2) to CreatureCode.EYE )
    }

    fun create(playerCreatures: Map<Position, CreatureCode>): Int {
        val match = Match(playerCreatures, iaCreatures())
        match.start()
        val matchId = matchs.add(match)
        return matchId
    }

}