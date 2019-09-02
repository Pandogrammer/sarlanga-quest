package pando.delivery.resources

import org.springframework.web.bind.annotation.*
import pando.creatures.CreatureCode
import pando.creatures.CreatureCodeMapper
import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.domain.CreatureCards
import pando.domain.MatchsService

@RestController
@RequestMapping("team")
class TeamCreationResource(private val cards: CreatureCards,
                           private val matchsService: MatchsService) {

    private val creatureCodeMapper = CreatureCodeMapper()
    private final val essence = 4

    @GetMapping("available")
    fun availableCreatures(): AvailableCreaturesResponse{
        return AvailableCreaturesResponse(essence, cards.creatures)
    }

    @PostMapping
    fun confirmTeam(@RequestHeader("Account-Id") accountId: String,
                    @RequestBody request: TeamRequest): TeamConfirmationResponse {
        println("AccountId: $accountId")

        if(request.creatures.isEmpty())
            throw RuntimeException("El equipo esta vacío.")

        val team = Team(essence)

        request.creatures.forEach {
            addCreature(team, it.position, it.creatureCode)?.let { response -> throw RuntimeException(response.message) }
        }

        var matchId = matchsService.create(team.creatures.map { it.key to it.value }.toMap())
        return TeamConfirmationResponse(matchId)
    }

    fun addCreature(team: Team, position: Position, creatureCode: CreatureCode): Response? {
        val creatureCard = creatureCodeMapper.toCard(creatureCode)

        if(!team.positionIsValid(position))
            return Response("Posicion inválida.")
        if(!team.positionIsEmpty(position))
            return Response("Posicion ocupada.")
        if(!cards.creatures.contains(creatureCard))
            return Response("La criatura no está disponible.")
        if(!team.essenceIsNotExceeded(creatureCard.stats.essence))
            return Response("La esencia de la criatura excede la disponible.")

        team.addCreature(position, creatureCard)
        return null
    }

}

class Team(val essence: Int = 0) {
    val creatures = HashMap<Position, CreatureCard>()
    var usedEssence = 0

    fun addCreature(position: Position, creatureCard: CreatureCard) {
        if (positionIsEmpty(position) && positionIsValid(position) && essenceIsNotExceeded(creatureCard.stats.essence)) {
            creatures[position] = creatureCard
            usedEssence += creatureCard.stats.essence
        }
    }

    fun positionIsEmpty(position: Position) = creatures[position] == null

    fun positionIsValid(position: Position): Boolean = position.column in 1..2 && position.line in 1..2

    fun essenceIsNotExceeded(creatureEssence: Int): Boolean = usedEssence + creatureEssence <= essence

}

class TeamRequest (val creatures: List<CreatureRequest>)

class CreatureRequest(val position: Position, val creatureCode: CreatureCode)

class AvailableCreaturesResponse (val essence: Int, val creatures: List<CreatureCard>)

class Response (val message: String)

class TeamConfirmationResponse(val matchId: Int)
