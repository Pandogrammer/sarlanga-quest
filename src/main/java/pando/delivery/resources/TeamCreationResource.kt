package pando.delivery.resources

import org.springframework.web.bind.annotation.*
import pando.creatures.CreatureCode
import pando.creatures.CreatureCodeMapper
import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.domain.CreatureCards
import pando.domain.MatchsService
import pando.domain.Team

@RestController
@RequestMapping("team")
class TeamCreationResource(private val cards: CreatureCards,
                           private val matchsService: MatchsService) {

    private val creatureCodeMapper = CreatureCodeMapper()
    private final val essence = 4
    val team = Team(essence)

    @GetMapping
    fun chosenCreatures(): TeamResponse {
        return TeamResponse(team.usedEssence, essence, team.creatures)
    }

    @GetMapping("available")
    fun availableCreatures(): AvailableCreaturesResponse{
        return AvailableCreaturesResponse(essence, cards.creatures)
    }

    @GetMapping("confirm")
    fun confirmTeam(): Response {
        if(team.creatures.isEmpty())
            return Response("El equipo esta vacío.")

        matchsService.create(team.creatures.map { it.key to it.value.creatureCode }.toMap())
        return Response("Equipo confirmado.")
    }

    @PostMapping
    fun addCreature(@RequestBody request: AddCreatureRequest): Response {
        val creatureCard = creatureCodeMapper.toCard(request.creatureCode)

        if(!team.positionIsValid(request.position))
            return Response("Posicion inválida.")
        if(!team.positionIsEmpty(request.position))
            return Response("Posicion ocupada.")
        if(!cards.creatures.contains(creatureCard))
            return Response("La criatura no está disponible.")
        if(!team.essenceIsNotExceeded(creatureCard.stats.essence))
            return Response("La esencia de la criatura excede la disponible.")

        team.addCreature(request.position, creatureCard)

        return Response("Criatura agregada.")
    }

    @DeleteMapping
    fun removeCreature(@RequestBody request: RemoveCreatureRequest): Response {
        if(team.getCreature(request.position) == null)
            return Response("Posicion vacía.")

        team.removeCreature(request.position)

        return Response("Criatura removida.")
    }
}

class TeamResponse (val usedEssence: Int, val maxEssence: Int, val creatures: Map<Position, CreatureCard>)

class RemoveCreatureRequest(val position: Position)

class AddCreatureRequest (val position: Position, val creatureCode: CreatureCode)

class AvailableCreaturesResponse (val essence: Int, val creatures: List<CreatureCard>)

class Response (val message: String)
