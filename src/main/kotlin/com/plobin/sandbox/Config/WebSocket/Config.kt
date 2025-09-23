package com.plobin.sandbox.Config.WebSocket

import com.plobin.sandbox.controller.Ssh.Connect.WebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration("sshWebSocketConfig")
@EnableWebSocket
class Config(
    private val sshWebSocketHandler: WebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(sshWebSocketHandler, "/ws/ssh/*")
            .setAllowedOrigins("*")
    }
}