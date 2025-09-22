package com.plobin.sandbox.controller.SandboxTemplateVersion.Update

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController("sandboxTemplateVersionUpdateController")
@RequestMapping("/api/sandbox-template-versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template Version", description = "샌드박스 템플릿 버전 관리 API")
class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 수정",
        description = "특정 ID의 ${ResourceNames.SANDBOX_TEMPLATE_VERSION} 정보를 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 수정됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "404", description = "템플릿 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "422", description = "유효성 검사 실패"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @PutMapping("/{id}")
    fun updateVersion(
        @Parameter(description = "템플릿 버전 ID", required = true)
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: Request
    ): ResponseEntity<Response> {
        val existingVersion = sandboxTemplateVersionRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val updatedVersion = existingVersion.copy(
            versionName = request.versionName,
            versionNumber = request.versionNumber,
            description = request.description,
            updatedAt = LocalDateTime.now()
        )

        val savedVersion = sandboxTemplateVersionRepository.save(updatedVersion)
        return ResponseEntity.ok(Response.fromEntity(savedVersion))
    }
}