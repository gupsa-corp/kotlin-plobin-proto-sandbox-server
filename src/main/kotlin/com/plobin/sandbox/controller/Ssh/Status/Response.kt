package com.plobin.sandbox.controller.Ssh.Status

import java.time.LocalDateTime

data class Response(
    val success: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val totalConnections: Int,
        val activeSessions: List<SessionStatus>
    )

    data class SessionStatus(
        val sessionId: String,
        val connectionId: String,
        val host: String,
        val port: Int,
        val username: String,
        val createdAt: LocalDateTime,
        val lastActivity: LocalDateTime,
        val isActive: Boolean
    )
}