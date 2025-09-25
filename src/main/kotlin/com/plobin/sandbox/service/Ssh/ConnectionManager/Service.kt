package com.plobin.sandbox.service.Ssh.ConnectionManager

import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.plobin.sandbox.Exception.Ssh.Connection.Exception
import com.plobin.sandbox.model.Ssh.Connection
import com.plobin.sandbox.model.Ssh.SessionInfo
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime
import java.util.*
import com.github.benmanes.caffeine.cache.Cache
import com.plobin.sandbox.config.CacheManagerConfig

@Service("sshConnectionManagerService")
class Service {
    private val activeConnections: Cache<String, Connection> = CacheManagerConfig.SshConnectionCache.create()
    private val sessionToConnectionMap: Cache<String, String> = CacheManagerConfig.SshConnectionCache.create()
    private val sessionInfoMap: Cache<String, SessionInfo> = CacheManagerConfig.SshConnectionCache.create()

    fun createConnection(sessionId: String, host: String, port: Int, username: String, password: String): Connection {
        try {
            val jsch = JSch()
            val session = jsch.getSession(username, host, port)
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect(30000)

            val channel = session.openChannel("shell") as ChannelShell
            channel.setPtyType("xterm-256color")
            channel.setPtySize(80, 24, 640, 480)
            channel.connect()

            val connectionId = UUID.randomUUID().toString()
            val connection = Connection(
                connectionId = connectionId,
                sessionId = sessionId,
                host = host,
                port = port,
                username = username,
                sshSession = session,
                channel = channel,
                createdAt = LocalDateTime.now(),
                lastActivity = LocalDateTime.now(),
                isActive = true
            )

            activeConnections.put(connectionId, connection)
            sessionToConnectionMap.put(sessionId, connectionId)

            return connection
        } catch (e: Exception) {
            throw Exception("SSH 연결 실패: ${e.message}")
        }
    }

    fun getConnection(sessionId: String): Connection? {
        val connectionId = sessionToConnectionMap.getIfPresent(sessionId) ?: return null
        return activeConnections.getIfPresent(connectionId)?.also {
            it.lastActivity = LocalDateTime.now()
        }
    }

    fun addWebSocketSession(sessionId: String, webSocketSession: WebSocketSession) {
        val connectionId = sessionToConnectionMap.getIfPresent(sessionId) ?: return
        val sessionInfo = SessionInfo(
            sessionId = sessionId,
            connectionId = connectionId,
            webSocketSession = webSocketSession,
            createdAt = LocalDateTime.now()
        )
        sessionInfoMap.put(sessionId, sessionInfo)
    }

    fun getWebSocketSession(sessionId: String): WebSocketSession? {
        return sessionInfoMap.getIfPresent(sessionId)?.webSocketSession
    }

    fun removeConnection(sessionId: String) {
        val connectionId = sessionToConnectionMap.getIfPresent(sessionId) ?: return
        val connection = activeConnections.getIfPresent(connectionId)

        connection?.let {
            try {
                it.channel?.disconnect()
                it.sshSession?.disconnect()
            } catch (e: Exception) {
                // 로그만 남기고 계속 진행
            }
        }

        activeConnections.invalidate(connectionId)
        sessionToConnectionMap.invalidate(sessionId)
        sessionInfoMap.invalidate(sessionId)
    }

    fun getAllActiveSessions(): List<String> {
        return sessionToConnectionMap.asMap().keys.toList()
    }

    @Scheduled(fixedRate = 1800000) // 5분마다 실행
    fun cleanupInactiveConnections() {
        val fiveMinutesAgo = LocalDateTime.now().minusMinutes(5)
        val inactiveConnections = activeConnections.asMap().values.filter {
            it.lastActivity.isBefore(fiveMinutesAgo)
        }

        inactiveConnections.forEach { connection ->
            connection.connectionId
            removeConnection(connection.sessionId)

        }
    }

    fun getConnectionCount(): Long {
        return activeConnections.estimatedSize()
    }
}
