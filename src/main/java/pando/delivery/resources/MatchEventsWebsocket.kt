package pando.delivery.resources

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler


class CombatHandler : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            println("Recibiste: ${message.payload}")
            send(session, message.payload)
        } catch (e: Exception) {
            e.printStackTrace()
            println(message.payload)
        }

    }


    private fun send(session: WebSocketSession, message: String) {
        try {
            println("Se envio: $message")
            session.sendMessage(TextMessage(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val sessionId = session.id
        println("$sessionId: CONNECTED")

        send(session, sessionId)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val sessionId = session.id
        println("$sessionId: DISCONNECTED")
    }
}

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(combatHandler(), "/combat") //.setAllowedOrigins("*")
    }


    @Bean
    fun combatHandler(): CombatHandler {
        return CombatHandler()
    }

}