package pando.delivery.resources

import pando.creatures.Creature
import pando.domain.Match
import pando.domain.Matchs
import org.springframework.web.bind.annotation.*
import pando.actions.Attack
import java.lang.RuntimeException

@RestController
@RequestMapping("match")
class MatchResource(private val matchs: Matchs) {

    @GetMapping("{matchId}")
    fun status(@PathVariable matchId: Int): MatchStatusResponse? {
        return matchs.find(matchId)?.let { MatchStatusResponse(it) }
    }

    @PostMapping
    fun creatureAction(@RequestBody request: ActionRequest) {
        val match = matchs.find(request.matchId)
        val action = Attack()
        match?.let{
            if(!it.validate(action, request.objectiveId))
                throw RuntimeException("Accion invalida.")

            it.actionExecution(Attack(), request.objectiveId)
        }
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

class ActionRequest(val matchId: Int, val objectiveId: Int)
