package com.plobin.sandbox.controller.SandboxTemplate.Get

import com.plobin.sandbox.Repository.SandboxTemplate.Entity
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController("sandboxTemplateGetController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 조회",
        description = "특정 ID의 ${ResourceNames.SANDBOX_TEMPLATE} 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 조회됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "404", description = "템플릿을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{id}")
    fun getTemplate(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable("id") id: Long
    ): Response {
        val template: Entity = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxTemplateException.notFound(id)
        return Response.fromEntity(template)
    }
}