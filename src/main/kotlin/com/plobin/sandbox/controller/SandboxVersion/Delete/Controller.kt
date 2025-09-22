package com.plobin.sandbox.controller.SandboxVersion.Delete

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Exception.SandboxVersion.Exception as SandboxVersionException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController("sandboxVersionDeleteController")
@RequestMapping("/api/sandbox-versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Version", description = "샌드박스 버전 관리 API")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_VERSION} 삭제",
        description = "특정 ID의 ${ResourceNames.SANDBOX_VERSION}을 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 삭제됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "404", description = "샌드박스 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteVersion(
        @Parameter(description = "샌드박스 버전 ID", required = true)
        @PathVariable("id") id: Long
    ): Response {
        val entity = sandboxVersionRepository.findById(id).orElse(null)
            ?: throw SandboxVersionException.notFound(id)

        sandboxVersionRepository.delete(entity)
        return Response.fromEntity(entity)
    }
}