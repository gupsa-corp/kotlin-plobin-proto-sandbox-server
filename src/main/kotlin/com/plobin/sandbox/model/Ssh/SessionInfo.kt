package com.plobin.sandbox.model.Ssh

import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime

data class SessionInfo(
    val sessionId: String,
    val connectionId: String,
    val webSocketSession: WebSocketSession?,
    val createdAt: LocalDateTime
)