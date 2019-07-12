package delivery.resources

import creatures.Creature
import creatures.Eye
import creatures.Skeleton
import delivery.resources.CreatureCode.*
import domain.Match
import domain.Matchs
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import turns.FirstTurn
import turns.NextTurn
import turns.RestingTurn
import turns.WinnerValidation


@RestController
@RequestMapping("create")
class CreateMatchResource (private val matchs: Matchs){

    @PostMapping
    fun create(@RequestBody request: CreateMatchRequest) : CreateMatchResponse {
        val aiTeam = 2
        val aiCreatures = listOf(Eye(team = aiTeam), Skeleton(team = aiTeam), Eye(team = aiTeam))

        val playerTeam = 1
        val playerCreatures = request.creatures.map { toCreature(it, playerTeam) }

        val match = Match(aiCreatures + playerCreatures)
        val matchId = matchs.add(match)

        return CreateMatchResponse(matchId)
    }

    private fun toCreature(code: CreatureCode, playerTeam: Int) : Creature {
        return when(code) {
            EYE -> Eye(playerTeam)
            SKELETON -> Skeleton(playerTeam)
        }
    }
}

class CreateMatchResponse(val matchId: Int)

class CreateMatchRequest (val creatures: List<CreatureCode>)

enum class CreatureCode {
    EYE, SKELETON
}
