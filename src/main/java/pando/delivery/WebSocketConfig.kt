package pando.delivery

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import pando.delivery.resources.CombatHandler
import pando.domain.MatchsService

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {

    @Autowired
    lateinit var combatHandler: CombatHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(combatHandler, "/combat") //.setAllowedOrigins("*")
    }


    @Bean
    fun combatHandler(matchsService: MatchsService): CombatHandler {
        return CombatHandler(matchsService)
    }

}