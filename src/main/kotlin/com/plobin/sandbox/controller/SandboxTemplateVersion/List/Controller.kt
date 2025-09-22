package com.plobin.sandbox.controller.SandboxTemplateVersion.List

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController("sandboxTemplateVersionListController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template Version", description = "샌드박스 템플릿 버전 관리 API")
class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 버전 목록 조회",
        description = "특정 템플릿의 모든 버전 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{templateId}/versions")
    fun listVersions(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable templateId: Long
    ): List<Response> {
        val versions = sandboxTemplateVersionRepository.findBySandboxTemplateId(templateId)
        return versions.map { Response.fromEntity(it) }
    }
}