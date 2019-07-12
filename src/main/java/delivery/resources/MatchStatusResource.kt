package delivery.resources

import creatures.Creature
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

class MatchStatusResponse(match: Match){
    val creatures: MutableMap<Int, CreatureStatusResponse> = mutableMapOf()
    var activeCreature: Int? = -1

    init {
        var id = 0
        match.creatures.forEach {
            creatures.put(id, CreatureStatusResponse(it))
            id++
        }
        match.activeCreature?.let { activeCreature = match.creatures.indexOf(it) }
    }
}

class CreatureStatusResponse(creature: Creature) {
    val health = creature.health
    val fatigue = creature.fatigue

}

