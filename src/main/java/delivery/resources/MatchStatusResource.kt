package delivery.resources

import domain.Match
import domain.Matchs
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("match-status")
class MatchStatusResource(private val matchs: Matchs) {

    @GetMapping("{matchId}")
    fun status(@PathVariable matchId: Int): MatchStatusResponse? {
        return matchs.find(matchId)?.let { MatchStatusResponse(it) }
    }

}

class MatchStatusResponse(val match: Match)

