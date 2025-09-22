package com.plobin.sandbox.controller.SandboxTemplateVersion.Info

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxTemplateVersionInfoController")
@RequestMapping("/api/sandbox-templates/versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "SandboxTemplateVersion", description = "샌드박스 템플릿 버전 관리 API")
class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    @Operation(
        summary = "샌드박스 템플릿 버전 상세 정보 조회",
        description = "특정 샌드박스 템플릿 버전의 폴더 경로 및 상세 정보를 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "템플릿 버전을 찾을 수 없음"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{id}/info")
    fun getSandboxTemplateVersionInfo(@PathVariable id: Long): Response {
        val version = sandboxTemplateVersionRepository.findById(id).orElse(null)
            ?: throw SandboxTemplateException.notFound(id)

        return Response.fromEntity(version)
    }
}