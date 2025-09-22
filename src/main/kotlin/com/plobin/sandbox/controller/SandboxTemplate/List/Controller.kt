package com.plobin.sandbox.controller.SandboxTemplate.List

import com.plobin.sandbox.Config.Swagger.Annotations.CommonResponses
import com.plobin.sandbox.Config.Swagger.Annotations.Tags
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxTemplateListController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 목록 조회",
        description = "삭제되지 않은 모든 ${ResourceNames.SANDBOX_TEMPLATE}의 목록을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping
    fun listTemplates(): List<Response> {
        val templates = sandboxTemplateRepository.findByDeletedAtIsNull()
        return templates.map { Response.fromEntity(it) }
    }
}