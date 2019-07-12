package delivery.resources

import actions.Attack
import domain.Matchs
import org.springframework.web.bind.annotation.*
import turns.CreatureAction
import turns.NextTurn

@RestController
@RequestMapping("action")
class CreatureActionResource(private val matchs: Matchs) {

    @PostMapping
    fun creatureAction(@RequestBody request: ActionRequest){
        val match = matchs.find(request.matchId)
        match?.creatureAction(request.creatureId, request.objectiveId)
    }

}

class ActionRequest(val matchId: Int, val creatureId: Int, val objectiveId: Int)


