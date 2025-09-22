package com.plobin.sandbox.controller.SandboxTemplate.Release

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController("sandboxTemplateReleaseController")
@RequestMapping("/api/sandbox-templates")
@Validated
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(
    private val sandboxTemplateRepository: SandboxTemplateRepository,
    private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository
) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 릴리즈 설정",
        description = "특정 템플릿의 특정 버전을 릴리즈 버전으로 설정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "릴리즈 설정이 성공적으로 완료됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            ApiResponse(responseCode = "404", description = "템플릿 또는 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @PostMapping("/{templateId}/release")
    fun setRelease(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable templateId: Long,

        @Valid @RequestBody request: Request
    ): ResponseEntity<Response> {

        // 템플릿 존재 확인
        val template = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(templateId)
            ?: throw SandboxTemplateException.notFound(templateId)

        // 버전 존재 확인 및 템플릿 ID 매칭 확인
        val version = sandboxTemplateVersionRepository.findById(request.versionId)
            .orElseThrow {
                RuntimeException("Template version with ID ${request.versionId} not found")
            }

        if (version.sandboxTemplateId != templateId) {
            throw RuntimeException("Version ${request.versionId} does not belong to template $templateId")
        }

        // TODO: 실제 릴리즈 설정 로직 구현
        // 1. 기존 릴리즈 버전이 있다면 해제
        // 2. 새 버전을 릴리즈로 설정
        // 3. 파일 시스템에서 릴리즈 폴더 업데이트

        val response = Response.fromEntities(template, version)
        return ResponseEntity.ok(response)
    }
}