package com.plobin.sandbox.controller.Ssh.Connect

import com.plobin.sandbox.service.Ssh.ConnectionManager.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController("sshConnectController")
@RequestMapping("/api/ssh")
@Tag(name = "SSH", description = "SSH 터미널 연결 관리 API")
class Controller(
    private val connectionManagerService: Service
) {

    @PostMapping("/connect")
    @Operation(summary = "SSH 연결 초기화", description = "SSH 서버에 연결하고 WebSocket URL을 반환합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "2", description = "SSH 연결 성공"),
            ApiResponse(responseCode = "400", description = "SSH 연결 실패"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    fun connect(@Valid @RequestBody request: Request): ResponseEntity<Response> {
        return try {
            val sessionId = UUID.randomUUID().toString()
            val connection = connectionManagerService.createConnection(
                sessionId = sessionId,
                host = request.host,
                port = request.port,
                username = request.username,
                password = request.password
            )

            val websocketUrl = "/ws/ssh/$sessionId"
            val response = Response(
                success = true,
                message = "SSH 연결이 성공적으로 초기화되었습니다",
                data = Response.Data(
                    sessionId = sessionId,
                    websocketUrl = websocketUrl,
                    connectionId = connection.connectionId
                )
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            val response = Response(
                success = false,
                message = "SSH 연결 실패: ${e.message}",
                data = null
            )
            ResponseEntity.badRequest().body(response)
        }
    }
}
