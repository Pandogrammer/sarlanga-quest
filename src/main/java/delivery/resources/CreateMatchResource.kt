package delivery.resources

import creatures.Creature
import creatures.Eye
import creatures.Skeleton
import delivery.resources.CreatureCode.EYE
import delivery.resources.CreatureCode.SKELETON
import domain.Matchs
import domain.MatchsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("add")
class CreateMatchResource (private val matchs: MatchsService){

    @PostMapping
    fun create(@RequestBody request: CreateMatchRequest) : CreateMatchResponse {
        val aiTeam = 2
        val aiCreatures = listOf(Eye(team = aiTeam), Skeleton(team = aiTeam), Eye(team = aiTeam))

        val playerTeam = 1
        val playerCreatures = request.creatures.map { toCreature(it, playerTeam) }

        val matchId = matchs.create(aiCreatures + playerCreatures)

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
