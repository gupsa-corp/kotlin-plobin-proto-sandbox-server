package com.plobin.sandbox.controller.Ssh.Disconnect

import com.plobin.sandbox.service.Ssh.ConnectionManager.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("sshDisconnectController")
@RequestMapping("/api/ssh")
@Tag(name = "SSH", description = "SSH 터미널 연결 관리 API")
class Controller(
    private val connectionManagerService: Service
) {

    @DeleteMapping("/disconnect/{sessionId}")
    @Operation(summary = "SSH 연결 종료", description = "지정된 세션의 SSH 연결을 강제로 종료합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "SSH 연결 종료 성공"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 세션"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    fun disconnect(@PathVariable sessionId: String): ResponseEntity<Response> {
        return try {
            val connection = connectionManagerService.getConnection(sessionId)
            if (connection != null) {
                connectionManagerService.removeConnection(sessionId)
                val response = Response(
                    success = true,
                    message = "SSH 연결이 성공적으로 종료되었습니다",
                    data = Response.Data(sessionId = sessionId)
                )
                ResponseEntity.ok(response)
            } else {
                val response = Response(
                    success = false,
                    message = "존재하지 않는 세션입니다",
                    data = null
                )
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            val response = Response(
                success = false,
                message = "SSH 연결 종료 실패: ${e.message}",
                data = null
            )
            ResponseEntity.badRequest().body(response)
        }
    }
}