package com.plobin.sandbox.controller.Ssh.Connect

import com.plobin.sandbox.service.Ssh.ConnectionManager.Service
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

@Component("sshWebSocketHandler")
class WebSocketHandler(
    private val connectionManagerService: Service
) : TextWebSocketHandler() {

    private val outputStreams = ConcurrentHashMap<String, OutputStream>()
    private val inputStreams = ConcurrentHashMap<String, InputStream>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val sessionId = extractSessionId(session.uri)
        if (sessionId != null) {
            connectionManagerService.addWebSocketSession(sessionId, session)
            val connection = connectionManagerService.getConnection(sessionId)

            connection?.let {
                val channel = it.channel
                if (channel != null) {
                    val outputStream = channel.outputStream
                    val inputStream = channel.inputStream

                    outputStreams[sessionId] = outputStream
                    inputStreams[sessionId] = inputStream

                    // SSH 출력을 WebSocket으로 전송하는 스레드 시작
                    startOutputReaderThread(sessionId, inputStream, session)
                }
            }
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val sessionId = extractSessionId(session.uri)
        if (sessionId != null) {
            val outputStream = outputStreams[sessionId]
            outputStream?.let {
                try {
                    it.write(message.payload.toByteArray())
                    it.flush()
                } catch (e: Exception) {
                    session.sendMessage(TextMessage("Error: ${e.message}"))
                }
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val sessionId = extractSessionId(session.uri)
        if (sessionId != null) {
            outputStreams.remove(sessionId)
            inputStreams.remove(sessionId)
            connectionManagerService.getConnection(sessionId)?: connectionManagerService.removeConnection(sessionId)
        }
    }

    private fun extractSessionId(uri: URI?): String? {
        val path = uri?.path ?: return null
        val segments = path.split("/")
        return segments.lastOrNull()
    }

    private fun startOutputReaderThread(sessionId: String, inputStream: InputStream, session: WebSocketSession) {
        Thread {
            try {
                val buffer = ByteArray(1024)
                while (session.isOpen && !Thread.currentThread().isInterrupted) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        val output = String(buffer, 0, bytesRead)
                        session.sendMessage(TextMessage(output))
                    }
                }
            } catch (e: Exception) {
                if (session.isOpen) {
                    try {
                        session.sendMessage(TextMessage("Connection lost: ${e.message}"))
                    } catch (ignored: Exception) {
                    }
                }
            }
        }.start()
    }
}
