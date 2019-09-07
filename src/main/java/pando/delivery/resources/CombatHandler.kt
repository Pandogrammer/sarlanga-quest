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
import java.util.*


class CombatHandler(val matchs: MatchsService) : TextWebSocketHandler() {

    val mapper = jacksonObjectMapper()
    val requests = PublishSubject.create<SessionMessage>()


    //estos tres deberia pasarlos a otro lado, ya que el otro socket tambien los usa
    private val session_account = HashMap<String, String>()
    private val account_session = HashMap<String, String>()

    init {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        connection()
        cards()
        reconnection()
        sendStatus()
        actionExecution()
    }

    private fun reconnection() {
        requests.filter { it.method == RECONNECTION }.subscribe { request ->
            val reconnectionRequest = mapper.readerFor(ReconnectionRequest::class.java).readValue<ReconnectionRequest>(request.message)
            val newSessionId = request.session.id
            val oldSessionId = reconnectionRequest.oldSessionId

            session_account[oldSessionId]?.let { accountId ->
                session_account[newSessionId] = accountId
                account_session[accountId] = newSessionId

                println(newSessionId + ": RECONNECTED - WAS [" + oldSessionId + "]")
            }
        }
    }

    private fun connection() {
        requests.filter { it.method == CONNECTION }.subscribe { request ->
            val sessionRequest = mapper.readerFor(ConnectionRequest::class.java).readValue<ConnectionRequest>(request.message)
            val sessionId = request.session.id
            val accountId = sessionRequest.accountId
            session_account[sessionId] = accountId
        }
    }


    private fun cards() {
        requests.filter { it.method == CARDS }.subscribe { message ->
            println("Enviando criaturas")
            val match = retrieveMatchFromSessionId(message.session.id)
            match?.let {
                send(message.session, CardsResponse(it.spawnedCreatures.map {
                    CreatureSpawn(it.id, it.position, it.team, it.card!!)
                }))
            }
        }
    }

    private fun actionExecution() {
        requests.filter{ it.method == ACTION }.subscribe { request ->
            val actionRequest = mapper.readerFor(ActionExecutionRequest::class.java).readValue<ActionExecutionRequest>(request.message)
            val match = retrieveMatchFromSessionId(request.session.id)
            val objectiveId = actionRequest.objectiveId

            match?.let {
                val action = Attack()
                if (it.validate(action, objectiveId)) {
                    it.actionExecution(action, objectiveId)
                }
            }
        }
    }

    private fun sendStatus() {
        requests.filter { it.method == STATUS }.subscribe { request ->
            println("Enviando estado")
            val match = retrieveMatchFromSessionId(request.session.id)
            match?.let {
                send(request.session, StatusResponse(it.spawnedCreatures.map {
                    CreatureStatus(it.id, it.health(), it.fatigue)
                }))
            }
        }
    }

    private fun retrieveMatchFromSessionId(sessionId: String): Match? {
        session_account[sessionId]?.let { accountId ->
            return matchs.get(accountId)
        }
        println("Match no encontrado")
        return null
    }

    private fun send(session: WebSocketSession, message: Any) {
        send(session, mapper.writeValueAsString(message))
    }

    private fun send(session: WebSocketSession, message: String) {
        try {
            println("Se envio: $message")
            session.sendMessage(TextMessage(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            println("Recibiste: ${message.payload}")
            val request = mapper.readerFor(SarlangaRequest::class.java).readValue<SarlangaRequest>(message.payload)
            requests.onNext(SessionMessage(session, request.method, message.payload))
        } catch (e: Exception) {
            e.printStackTrace()
            println(message.payload)
        }

    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val sessionId = session.id
        println("$sessionId: CONNECTED")

        send(session, NewSessionResponse(sessionId))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val sessionId = session.id
        println("$sessionId: DISCONNECTED")
    }

}


class CreatureSpawn(val id: Int, val position: Position, val team: Int, val card: CreatureCard)

class CreatureStatus(val id: Int, val health: Int, val fatigue: Int)

data class NewSessionResponse(val sessionId: String) : SarlangaRequest(SESSION)

data class CardsResponse(val creatures: List<CreatureSpawn>) : SarlangaRequest(CARDS)

data class StatusResponse(val creatures: List<CreatureStatus>)

data class SessionMessage(val session: WebSocketSession, val method: SarlangaMethod, val message: String)

open class SarlangaRequest(val method: SarlangaMethod)

enum class SarlangaMethod {
    STATUS, ACTION, CONNECTION, RECONNECTION, CARDS, SESSION
}

data class ActionExecutionRequest(val objectiveId: Int)

data class ConnectionRequest(val accountId: String)

data class ReconnectionRequest(val oldSessionId: String)