package com.plobin.sandbox.controller.Sandbox.Delete

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
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
import java.io.File
import java.time.LocalDateTime

@RestController("sandboxDeleteController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(
    private val sandboxRepository: SandboxRepository,
    private val sandboxVersionRepository: SandboxVersionRepository
) {

    @Operation(
        summary = "${ResourceNames.SANDBOX} 삭제",
        description = "특정 ID의 ${ResourceNames.SANDBOX}를 삭제합니다. (Soft Delete)"
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
            ApiResponse(responseCode = "404", description = "샌드박스를 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteSandbox(
        @Parameter(description = "샌드박스 ID", required = true)
        @PathVariable("id") id: Long
    ): Response {
        val entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)

        // 1. 물리 파일 삭제
        deletePhysicalFiles(entity.folderPath)

        // 2. 관련된 모든 버전 삭제
        val versions = sandboxVersionRepository.findBySandboxId(id)
        sandboxVersionRepository.deleteAll(versions)

        // 3. 샌드박스 완전 삭제
        sandboxRepository.delete(entity)

        return Response.fromEntity(entity)
    }

    private fun deletePhysicalFiles(folderPath: String) {
        try {
            val folder = File(folderPath)
            if (folder.exists() && folder.isDirectory) {
                folder.deleteRecursively()
            }
        } catch (e: Exception) {
            // 파일 삭제 실패는 로그만 남기고 계속 진행
            println("파일 삭제 실패: ${folderPath}, 에러: ${e.message}")
        }
    }

    operator fun invoke(id: Long): Response = deleteSandbox(id)
}