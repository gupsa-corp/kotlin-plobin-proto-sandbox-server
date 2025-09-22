package com.plobin.sandbox.controller.Sandbox.Get

import com.plobin.sandbox.Repository.Sandbox.Entity
import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController("sandboxGetController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(private val sandboxRepository: SandboxRepository) {

    @Operation(
        summary = "${ResourceNames.SANDBOX} 조회",
        description = "특정 ID의 ${ResourceNames.SANDBOX} 정보를 조회합니다."
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
            ApiResponse(responseCode = "404", description = "샌드박스를 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{id}")
    fun getSandbox(
        @Parameter(description = "샌드박스 ID", required = true)
        @PathVariable("id") id: Long
    ): Response {
        val sandbox: Entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)
        return Response.fromEntity(sandbox)
    }
}