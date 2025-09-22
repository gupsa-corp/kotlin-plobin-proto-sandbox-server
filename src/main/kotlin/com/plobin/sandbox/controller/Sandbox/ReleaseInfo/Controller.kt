package com.plobin.sandbox.controller.Sandbox.ReleaseInfo

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxReleaseInfoController")
@RequestMapping("/api/sandboxes")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox", description = "샌드박스 관리 API")
class Controller(
    private val sandboxRepository: SandboxRepository,
    private val sandboxVersionRepository: SandboxVersionRepository
) {

    @Operation(
        summary = "샌드박스 Release 버전 정보 조회",
        description = "특정 샌드박스의 현재 Release 버전 정보를 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "샌드박스를 찾을 수 없음"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping("/{id}/release-info")
    fun getSandboxReleaseInfo(@PathVariable id: Long): Response {
        val sandbox = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)

        val currentVersion = sandboxVersionRepository.findBySandboxIdAndIsCurrent(id, true)

        return Response.fromSandboxAndVersion(sandbox, currentVersion)
    }
}