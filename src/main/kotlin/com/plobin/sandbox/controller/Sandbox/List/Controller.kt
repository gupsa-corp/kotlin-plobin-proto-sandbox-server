package com.plobin.sandbox.controller.Sandbox.List

import com.plobin.sandbox.Config.Swagger.Annotations.CommonResponses
import com.plobin.sandbox.Config.Swagger.Annotations.Tags
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxListController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(private val sandboxRepository: SandboxRepository) {

    @Operation(
        summary = "샌드박스 목록 조회",
        description = "삭제되지 않은 모든 샌드박스의 목록을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping
    fun listSandboxes(): List<Response> {
        val sandboxes = sandboxRepository.findByDeletedAtIsNull()
        return sandboxes.map { Response.fromEntity(it) }
    }
}