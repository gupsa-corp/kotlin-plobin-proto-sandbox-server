package com.plobin.sandbox.controller.Ssh.Status

import com.plobin.sandbox.service.Ssh.ConnectionManager.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("sshStatusController")
@RequestMapping("/api/ssh")
@Tag(name = "SSH", description = "SSH 터미널 연결 관리 API")
class Controller(
    private val connectionManagerService: Service
) {

    @GetMapping("/status")
    @Operation(summary = "SSH 연결 상태 조회", description = "현재 활성 상태인 모든 SSH 연결의 상태를 조회합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "SSH 연결 상태 조회 성공"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    fun getStatus(): ResponseEntity<Response> {
        return try {
            val activeSessions = connectionManagerService.getAllActiveSessions()
            val sessionStatuses = activeSessions.mapNotNull { sessionId ->
                connectionManagerService.getConnection(sessionId)?.let { connection ->
                    Response.SessionStatus(
                        sessionId = connection.sessionId,
                        connectionId = connection.connectionId,
                        host = connection.host,
                        port = connection.port,
                        username = connection.username,
                        createdAt = connection.createdAt,
                        lastActivity = connection.lastActivity,
                        isActive = connection.isActive
                    )
                }
            }

            val response = Response(
                success = true,
                message = "SSH 연결 상태를 성공적으로 조회했습니다",
                data = Response.Data(
                    totalConnections = connectionManagerService.getConnectionCount(),
                    activeSessions = sessionStatuses
                )
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            val response = Response(
                success = false,
                message = "SSH 연결 상태 조회 실패: ${e.message}",
                data = null
            )
            ResponseEntity.badRequest().body(response)
        }
    }
}