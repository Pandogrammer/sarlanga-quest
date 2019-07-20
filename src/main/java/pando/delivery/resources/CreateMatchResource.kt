package pando.delivery.resources

import pando.creatures.CreatureCode
import pando.domain.MatchsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("add")
class CreateMatchResource (private val matchsService: MatchsService){

    @PostMapping
    fun create(@RequestBody request: CreateMatchRequest) : CreateMatchResponse {
        val aiTeam = 2
        val aiCreatures = listOf(CreatureCode.EYE, CreatureCode.SKELETON, CreatureCode.EYE)

        val playerTeam = 1
        val playerCreatures = request.creatures

        val matchId = matchsService.create(playerCreatures, aiCreatures)

        return CreateMatchResponse(matchId)
    }

}

class CreateMatchResponse(val matchId: Int)

class CreateMatchRequest (val creatures: List<CreatureCode>)

