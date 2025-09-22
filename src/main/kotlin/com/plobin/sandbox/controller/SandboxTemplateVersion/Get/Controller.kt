package com.plobin.sandbox.controller.SandboxTemplateVersion.Get

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("sandboxTemplateVersionGetController")
@RequestMapping("/api/sandbox-template-versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template Version", description = "샌드박스 템플릿 버전 관리 API")
class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 조회",
        description = "특정 ID의 ${ResourceNames.SANDBOX_TEMPLATE_VERSION} 정보를 조회합니다."
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
            ApiResponse(responseCode = "404", description = "템플릿 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{id}")
    fun getVersion(
        @Parameter(description = "템플릿 버전 ID", required = true)
        @PathVariable("id") id: Long
    ): ResponseEntity<Response> {
        val version = sandboxTemplateVersionRepository.findById(id).orElse(null)
        return if (version != null) {
            ResponseEntity.ok(Response.fromEntity(version))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}