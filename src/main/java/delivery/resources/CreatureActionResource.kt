package delivery.resources

import domain.Matchs
import domain.MatchsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("action")
class CreatureActionResource(private val matchs: Matchs) {

    @PostMapping
    fun creatureAction(@RequestBody request: ActionRequest){
        val match = matchs.find(request.matchId)
        match?.creatureAction(request.objectiveId)
    }

}

class ActionRequest(val matchId: Int, val objectiveId: Int)


