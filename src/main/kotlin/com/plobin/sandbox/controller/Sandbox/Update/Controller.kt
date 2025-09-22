package com.plobin.sandbox.controller.Sandbox.Update

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
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

@RestController("sandboxUpdateController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(private val sandboxRepository: SandboxRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX} 수정",
        description = "특정 ID의 ${ResourceNames.SANDBOX} 정보를 수정합니다."
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
            ApiResponse(responseCode = "404", description = "샌드박스를 찾을 수 없음"),
            ApiResponse(responseCode = "422", description = "유효성 검사 실패"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @PutMapping("/{id}")
    fun updateSandbox(
        @Parameter(description = "샌드박스 ID", required = true)
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: Request
    ): Response {
        val entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)

        val updatedEntity = entity.copy(
            name = request.name ?: entity.name,
            description = request.description ?: entity.description,
            folderPath = request.folderPath ?: entity.folderPath,
            status = request.status ?: entity.status,
            isActive = request.isActive ?: entity.isActive,
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxRepository.save(updatedEntity)
        return Response.fromEntity(savedEntity)
    }
}