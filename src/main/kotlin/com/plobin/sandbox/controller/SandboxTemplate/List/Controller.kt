package com.plobin.sandbox.controller.SandboxTemplate.List

import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sandbox-templates")
@Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    @Operation(
        summary = "샌드박스 템플릿 목록 조회",
        description = "삭제되지 않은 모든 샌드박스 템플릿의 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping
    fun listTemplates(): List<Response> {
        val templates = sandboxTemplateRepository.findByDeletedAtIsNull()
        return templates.map { Response.fromEntity(it) }
    }
}