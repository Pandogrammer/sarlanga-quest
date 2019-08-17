package pando.delivery.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.subjects.PublishSubject
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import pando.actions.Attack
import pando.creatures.Creature
import pando.creatures.Position
import pando.domain.MatchsService
import java.util.*


class CombatHandler(val matchs: MatchsService) : TextWebSocketHandler() {

    val mapper = jacksonObjectMapper()
    val requests = PublishSubject.create<SessionMessage>()


    //estos tres deberia pasarlos a otro lado, ya que el otro socket tambien los usa
    private val session_websocketsession = HashMap<String, WebSocketSession>()
    private val session_account = HashMap<String, String>()
    private val account_session = HashMap<String, String>()
    private val account_team = HashMap<String, Int>()

    init {
        connection()
        reconnection()
        sendStatus()
        actionExecution()
    }

    private fun reconnection() {
        requests.filter { it.request.method == SarlangaMethod.RECONNECTION }.subscribe { message ->

            val newSessionId = message.session.id
            val oldSessionId = message.request.attributes!!["oldSessionId"] as String

            session_account[oldSessionId]?.let { accountId ->
                session_account[newSessionId] = accountId
                account_session[accountId] = newSessionId

                println(newSessionId + ": RECONNECTED - WAS [" + oldSessionId + "]")
            }
        }
    }


    private fun connection() {
        requests.filter { it.request.method == SarlangaMethod.CONNECTION }.subscribe { message ->

        }
    }

    private fun actionExecution() {
        requests.filter { it.request.method == SarlangaMethod.ACTION }.subscribe { message ->
            val match = matchs.get(message.request.attributes!!["matchId"] as Int)
            val objectiveId = message.request.attributes!!["objectiveId"] as Int

            match?.let {
                val action = Attack()
                if (it.validate(action, objectiveId)) {
                    it.actionExecution(action, objectiveId)
                }
            }
        }
    }

    private fun sendStatus() {
        requests.filter { it.request.method == SarlangaMethod.STATUS }.subscribe { message ->
            val match = matchs.get(message.request.attributes!!["matchId"] as Int)
            match?.let { send(message.session, StatusResponse(it.creatures.map { CreatureStatus(it) })) }
        }
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
            requests.onNext(SessionMessage(session, request))
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

class CreatureStatus(creature: Creature) {
    val team: Int = creature.team
    val position: Position = creature.position
    val health: Int = creature.health()
    val fatigue: Int = creature.fatigue
    val speed: Int = creature.stats.speed
}

data class NewSessionResponse(val sessionId: String)

data class StatusResponse(val creatures: List<CreatureStatus>)

data class SessionMessage(val session: WebSocketSession, val request: SarlangaRequest)

data class SarlangaRequest(val method: SarlangaMethod, val attributes: Map<String, Any>?) //caca

enum class SarlangaMethod {
    STATUS, ACTION, CONNECTION, RECONNECTION
}

