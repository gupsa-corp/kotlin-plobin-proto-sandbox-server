package com.plobin.sandbox.controller.Ssh.Connect

import com.fasterxml.jackson.databind.ObjectMapper
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
            try {
                // JSON 메시지인지 확인 (터미널 크기 변경)
                val objectMapper = ObjectMapper()
                val jsonMessage = objectMapper.readTree(message.payload)
                if (jsonMessage.has("type") && jsonMessage.get("type").asText() == "resize") {
                    val cols = jsonMessage.get("cols").asInt()
                    val rows = jsonMessage.get("rows").asInt()
                    // SSH 채널에 터미널 크기 설정
                    resizeTerminal(sessionId, cols, rows)
                    // 클라이언트에 응답 전송
                    session.sendMessage(TextMessage(objectMapper.writeValueAsString(mapOf("type" to "resize_response", "status" to "success"))))
                    return
                }
            } catch (e: Exception) {
                // JSON이 아닌 일반 입력 처리
            }

            // 일반 터미널 입력 처리
            val outputStream = outputStreams[sessionId]
            outputStream?.let {
                try {
                    it.write(message.payload.toByteArray(Charsets.UTF_8))
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
                val buffer = ByteArray(4096)
                val stringBuilder = StringBuilder()

                while (session.isOpen && !Thread.currentThread().isInterrupted) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        try {
                            val chunk = String(buffer, 0, bytesRead, Charsets.UTF_8)

                            // 완전한 문자열이 될 때까지 버퍼링
                            stringBuilder.append(chunk)

                            // 라인 단위로 처리하거나 버퍼가 일정 크기에 도달하면 전송
                            val bufferedContent = stringBuilder.toString()
                            if (bufferedContent.contains('\n') || bufferedContent.contains('\r') || stringBuilder.length > 512) {
                                session.sendMessage(TextMessage(bufferedContent))
                                stringBuilder.clear()
                            }
                        } catch (encodingError: Exception) {
                            // UTF-8 디코딩 실패 시 누적된 데이터 먼저 전송
                            if (stringBuilder.isNotEmpty()) {
                                session.sendMessage(TextMessage(stringBuilder.toString()))
                                stringBuilder.clear()
                            }
                            // 실패한 바이트를 ISO-8859-1로 처리
                            val output = String(buffer, 0, bytesRead, Charsets.ISO_8859_1)
                            session.sendMessage(TextMessage(output))
                        }
                    }
                }

                // 남은 데이터 전송
                if (stringBuilder.isNotEmpty()) {
                    session.sendMessage(TextMessage(stringBuilder.toString()))
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

    private fun resizeTerminal(sessionId: String, cols: Int, rows: Int) {
        try {
            val connection = connectionManagerService.getConnection(sessionId)
            connection?.let {
                val channel = it.channel
                if (channel != null && channel.isConnected) {
                    channel.setPtySize(cols, rows, cols * 8, rows * 16)
                }
            }
        } catch (e: Exception) {
            println("Failed to resize terminal for session $sessionId: ${e.message}")
        }
    }
}
