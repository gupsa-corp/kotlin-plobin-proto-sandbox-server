package com.plobin.sandbox.controller.SandboxVersion.Create

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Repository.SandboxVersion.Entity
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController("sandboxVersionCreateController")
@RequestMapping("/api/sandbox-versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Version", description = "샌드박스 버전 관리 API")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_VERSION} 생성",
        description = "새로운 ${ResourceNames.SANDBOX_VERSION}을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "성공적으로 생성됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "422", description = "유효성 검사 실패"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @PostMapping
    fun createVersion(@Valid @RequestBody request: Request): Response {
        val entity = Entity(
            sandboxId = request.sandboxId,
            templateVersionId = request.templateVersionId,
            versionName = request.versionName,
            versionPath = request.versionPath,
            description = request.description,
            isCurrent = request.isCurrent ?: false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxVersionRepository.save(entity)
        return Response.fromEntity(savedEntity)
    }

    operator fun invoke(request: Request): Response = createSandboxVersion(request)
}