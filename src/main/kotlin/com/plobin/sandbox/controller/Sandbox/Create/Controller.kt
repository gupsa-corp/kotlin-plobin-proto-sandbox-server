package com.plobin.sandbox.controller.Sandbox.Create

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.Sandbox.Entity
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

@RestController("sandboxCreateController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(private val sandboxRepository: SandboxRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX} 생성",
        description = "새로운 ${ResourceNames.SANDBOX}를 생성합니다."
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
    fun createSandbox(@Valid @RequestBody request: Request): Response {
        val entity = Entity(
            uuid = request.uuid,
            name = request.name,
            description = request.description,
            folderPath = request.folderPath,
            templateId = request.templateId,
            status = request.status,
            createdBy = request.createdBy,
            isActive = request.isActive ?: true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxRepository.save(entity)
        return Response.fromEntity(savedEntity)
    }
}