package com.plobin.sandbox.controller.SandboxTemplate.Serve

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

@RestController("sandboxTemplateServeController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(
    private val sandboxTemplateRepository: SandboxTemplateRepository,
    private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository
) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 파일 서빙",
        description = """
            템플릿 파일을 다운로드합니다.
            versionId가 지정되지 않으면 현재 릴리즈 버전을 서빙합니다.
            ZIP 형태로 압축되어 제공됩니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "파일이 성공적으로 서빙됨",
                content = [Content(
                    mediaType = "application/zip"
                )]
            ),
            ApiResponse(responseCode = "404", description = "템플릿 또는 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "파일 처리 중 서버 오류")
        ]
    )
    @GetMapping("/{templateId}/serve")
    fun serveTemplate(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable templateId: Long,

        @Parameter(description = "버전 ID (선택사항, 미지정시 릴리즈 버전)")
        @RequestParam(required = false) versionId: Long?
    ): ResponseEntity<InputStreamResource> {

        // 템플릿 존재 확인
        val template = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(templateId)
            ?: throw SandboxTemplateException.notFound(templateId)

        // 버전 결정 (지정된 버전 또는 릴리즈 버전)
        val version = if (versionId != null) {
            val requestedVersion = sandboxTemplateVersionRepository.findById(versionId)
                .orElseThrow {
                    RuntimeException("Template version with ID $versionId not found")
                }

            if (requestedVersion.sandboxTemplateId != templateId) {
                throw RuntimeException("Version $versionId does not belong to template $templateId")
            }

            requestedVersion
        } else {
            // TODO: 릴리즈 버전 조회 로직 구현
            // 현재는 가장 최근 버전을 가져옴
            sandboxTemplateVersionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId)
                ?: throw RuntimeException("No versions found for template $templateId")
        }

        // 파일 경로 구성
        val filePath = Paths.get(template.sandboxFullFolderPath, version.versionNumber)

        if (!Files.exists(filePath)) {
            throw RuntimeException("Template files not found at path: $filePath")
        }

        // TODO: 실제 ZIP 파일 생성 또는 기존 ZIP 파일 제공
        // 현재는 디렉토리가 있다고 가정하고 임시 구현
        val zipFileName = "${template.sandboxFolderName}_${version.versionNumber}.zip"

        // 임시로 빈 파일 생성 (실제로는 ZIP 압축 로직 필요)
        val tempFile = File.createTempFile("template_", ".zip")
        tempFile.deleteOnExit()

        val resource = InputStreamResource(FileInputStream(tempFile))

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFileName\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(tempFile.length())
            .body(resource)
    }

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 서빙 정보 조회",
        description = "템플릿 파일 서빙 정보를 JSON으로 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "서빙 정보가 성공적으로 조회됨",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(responseCode = "404", description = "템플릿 또는 버전을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{templateId}/serve/info")
    fun getServeInfo(
        @Parameter(description = "템플릿 ID", required = true)
        @PathVariable templateId: Long,

        @Parameter(description = "버전 ID (선택사항, 미지정시 릴리즈 버전)")
        @RequestParam(required = false) versionId: Long?
    ): ResponseEntity<Response> {

        // 템플릿 존재 확인
        val template = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(templateId)
            ?: throw SandboxTemplateException.notFound(templateId)

        // 버전 결정
        val version = if (versionId != null) {
            val requestedVersion = sandboxTemplateVersionRepository.findById(versionId)
                .orElseThrow {
                    RuntimeException("Template version with ID $versionId not found")
                }

            if (requestedVersion.sandboxTemplateId != templateId) {
                throw RuntimeException("Version $versionId does not belong to template $templateId")
            }

            requestedVersion
        } else {
            // TODO: 릴리즈 버전 조회 로직 구현
            sandboxTemplateVersionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId)
                ?: throw RuntimeException("No versions found for template $templateId")
        }

        val downloadUrl = "/api/sandbox-templates/$templateId/serve${if (versionId != null) "?versionId=$versionId" else ""}"
        val fileSize = 0L // TODO: 실제 파일 크기 계산

        val response = Response.fromEntities(template, version, downloadUrl, fileSize)

        return ResponseEntity.ok(response)
    }
}