package com.plobin.sandbox.controller.SandboxTemplate.Update

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController("sandboxTemplateUpdateController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 수정",
        description = "특정 ID의 ${ResourceNames.SANDBOX_TEMPLATE} 정보를 수정합니다."
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
            ApiResponse(responseCode = "404", description = "템플릿을 찾을 수 없음"),
            ApiResponse(responseCode = "422", description = "유효성 검사 실패"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @PutMapping("/{id}")
    fun updateTemplate(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: Request
    ): Response {
        val existingTemplate = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxTemplateException.notFound(id)

        val updatedTemplate = existingTemplate.copy(
            sandboxFolderName = request.sandboxFolderName,
            sandboxFolderPath = request.sandboxFolderPath,
            sandboxFullFolderPath = request.sandboxFullFolderPath,
            sandboxStatus = request.sandboxStatus,
            description = request.description,
            isActive = request.isActive ?: existingTemplate.isActive,
            updatedAt = LocalDateTime.now()
        )

        val savedTemplate = sandboxTemplateRepository.save(updatedTemplate)
        return Response.fromEntity(savedTemplate)
    }
}