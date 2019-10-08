package pando.delivery.resources

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.subjects.PublishSubject
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import pando.actions.Attack
import pando.creatures.Position
import pando.creatures.cards.CreatureCard
import pando.delivery.resources.SarlangaMethod.*
import pando.domain.Match
import pando.domain.MatchsService

class CombatHandler(val matchs: MatchsService) : TextWebSocketHandler() {

    val mapper = jacksonObjectMapper()
    val requests = PublishSubject.create<SessionMessage>()


    //que hago con toda esta caca????
    private val sessionId_accountId = HashMap<String, String>()
    private val accountId_sessionId = HashMap<String, String>()
    private val sessionId_websocketSession = HashMap<String, WebSocketSession>()
    private val accountId_matchId = HashMap<String, Int>()
    private val matchId_accountId = HashMap<Int, String>()
    //CAAAAAAAACAAAAAAAAAAAAAAAA

    init {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        sendMatchEvents()

        connectionMessage()
        reconnectionMessage()
        cardsMessage()
        statusMessage()
        actionExecutionMessage()
        activeCreatureMessage()
    }

    private fun sendMatchEvents() {
        matchs.matchCreated.subscribe { matchCreation ->
            val accountId = matchCreation.accountId
            val matchId = matchCreation.matchId
            accountId_matchId[accountId] = matchId
            matchId_accountId[matchId] = accountId

            val match = matchs.get(matchCreation.matchId)!!

            match.events.actions.subscribe { event ->
                getSessionFromMatchId(matchId)?.let {
                    val actorId = event.actor.id
                    val actionId = 0 //alv momentaneamente
                    val targetId = event.target.id
                    val roll = event.roll
                    val message = ActionExecutionMessage(actorId, actionId, targetId, roll)
                    send(it, message)
                }
            }

            match.events.matchEnd.subscribe { event ->
                getSessionFromMatchId(matchId)?.let {
                    val team = event.winningTeam
                    val message = MatchEndMessage(team)
                    send(it, message)
                }
            }


            match.events.activeCreature.subscribe { event ->
                getSessionFromMatchId(matchId)?.let {
                    val creatureId = event.id
                    val message = ActiveCreatureMessage(creatureId)
                    send(it, message)
                }
            }
        }
    }

    private fun getSessionFromMatchId(matchId: Int): WebSocketSession? {
        val accountId = matchId_accountId[matchId]
        val sessionId = accountId_sessionId[accountId]
        val session = sessionId_websocketSession[sessionId]
        return session
    }


    private fun reconnectionMessage() {
        requests.filter { it.method == RECONNECTION }.subscribe { request ->
            val reconnectionRequest = mapper.readerFor(ReconnectionRequest::class.java).readValue<ReconnectionRequest>(request.message)
            val newSessionId = request.session.id
            val oldSessionId = reconnectionRequest.oldSessionId

            sessionId_accountId[oldSessionId]?.let { accountId ->
                sessionId_accountId[newSessionId] = accountId
                accountId_sessionId[accountId] = newSessionId

                println(newSessionId + ": RECONNECTED - WAS [" + oldSessionId + "]")
            }
        }
    }

    private fun connectionMessage() {
        requests.filter { it.method == CONNECTION }.subscribe { request ->
            val sessionRequest = mapper.readerFor(ConnectionRequest::class.java).readValue<ConnectionRequest>(request.message)
            val sessionId = request.session.id
            val accountId = sessionRequest.accountId
            sessionId_accountId[sessionId] = accountId
            accountId_sessionId[accountId] = sessionId
        }
    }


    private fun cardsMessage() {
        requests.filter { it.method == CARDS }.subscribe { message ->
            val match = retrieveMatchFromSessionId(message.session.id)
            match?.let {
                send(message.session, CardsResponse(it.spawnedCreatures.map {
                    CreatureSpawn(it.id, it.position, it.team, it.card!!)
                }))
            }
        }
    }

    private fun actionExecutionMessage() {
        requests.filter { it.method == ACTION }.subscribe { request ->
            val actionRequest = mapper.readerFor(ActionExecutionRequest::class.java).readValue<ActionExecutionRequest>(request.message)
            val match = retrieveMatchFromSessionId(request.session.id)
            val objectiveId = actionRequest.targetId

            match?.let {
                val action = Attack()
                if (it.validate(action, objectiveId)) {
                    it.actionExecution(action, objectiveId)
                }
            }
        }
    }

    private fun statusMessage() {
        requests.filter { it.method == STATUS }.subscribe { request ->
            val match = retrieveMatchFromSessionId(request.session.id)
            match?.let {
                send(request.session, StatusResponse(it.spawnedCreatures.map {
                    CreatureStatus(it.id, it.damageCounters, it.fatigue)
                }))
            }
        }
    }

    private fun activeCreatureMessage(){
        requests.filter { it.method == ACTIVE_CREATURE }.subscribe { request ->
            val match = retrieveMatchFromSessionId(request.session.id)
            match?.run {
                activeSpawnedCreature?.run {
                    send(request.session, ActiveCreatureMessage(id))
                }
            }
        }
    }

    private fun retrieveMatchFromSessionId(sessionId: String): Match? {
        sessionId_accountId[sessionId]?.let { accountId ->
            accountId_matchId[accountId]?.let {
                return matchs.get(it)
            }
        }
        println("Match no encontrado")
        return null
    }

    private fun send(session: WebSocketSession, message: Any) {
        send(session, mapper.writeValueAsString(message))
    }

    private fun send(session: WebSocketSession, message: String) {
        try {
            println("Enviando: $message")
            session.sendMessage(TextMessage(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            println("Recibiste: ${message.payload}")
            val request = mapper.readerFor(SarlangaMessage::class.java).readValue<SarlangaMessage>(message.payload)
            requests.onNext(SessionMessage(session, request.method, message.payload))
        } catch (e: Exception) {
            e.printStackTrace()
            println(message.payload)
        }

    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val sessionId = session.id
        println("$sessionId: CONNECTED")

        sessionId_websocketSession[sessionId] = session
        send(session, NewSessionResponse(sessionId))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val sessionId = session.id
        sessionId_websocketSession.remove(sessionId)
        println("$sessionId: DISCONNECTED")
    }

}


class CreatureSpawn(val id: Int, val position: Position, val team: Int, val card: CreatureCard)

class CreatureStatus(val id: Int, val damage: Int, val fatigue: Int)

data class NewSessionResponse(val sessionId: String) : SarlangaMessage(SESSION)

data class CardsResponse(val creatures: List<CreatureSpawn>) : SarlangaMessage(CARDS)

data class StatusResponse(val creatures: List<CreatureStatus>) : SarlangaMessage(STATUS)

data class SessionMessage(val session: WebSocketSession, val method: SarlangaMethod, val message: String)

open class SarlangaMessage(val method: SarlangaMethod)

data class ActionExecutionRequest(val targetId: Int)

data class ConnectionRequest(val accountId: String)

data class ReconnectionRequest(val oldSessionId: String)

data class ActionExecutionMessage(val actorId: Int, val actionId: Int, val targetId: Int, val roll: Int) : SarlangaMessage(ACTION_EXECUTION)

data class MatchEndMessage(val team: Int) : SarlangaMessage(MATCH_END)

data class ActiveCreatureMessage(val creatureId: Int) : SarlangaMessage(ACTIVE_CREATURE)

enum class SarlangaMethod {
    STATUS,
    ACTION,
    CONNECTION,
    RECONNECTION,
    CARDS,
    SESSION,
    MATCH_END,
    ACTION_EXECUTION,
    ACTIVE_CREATURE
}
