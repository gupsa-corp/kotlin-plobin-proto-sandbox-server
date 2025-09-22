package com.plobin.sandbox.controller.SandboxVersion.List

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxVersionListController")
@RequestMapping("/api/sandbox-versions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Version", description = "샌드박스 버전 관리 API")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    @Operation(
        summary = "샌드박스 버전 목록 조회",
        description = "특정 샌드박스의 모든 버전 목록을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
            io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
        ]
    )
    @GetMapping
    fun listVersions(@RequestParam sandboxId: Long): List<Response> {
        val versions = sandboxVersionRepository.findBySandboxIdOrderByCreatedAtDesc(sandboxId)
        return versions.map { Response.fromEntity(it) }
    }
}