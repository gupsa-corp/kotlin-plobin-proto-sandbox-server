package com.plobin.sandbox.controller.SandboxTemplate.Create

import com.plobin.sandbox.service.SandboxTemplate.Create.Service as CreateService
import com.plobin.sandbox.Config.Swagger.Annotations.ResourceNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController("sandboxTemplateCreateController")
@RequestMapping("/api/sandbox-templates")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
class Controller(private val createService: CreateService) {

    @Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 생성",
        description = "새로운 ${ResourceNames.SANDBOX_TEMPLATE}을 생성합니다."
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
    fun createTemplate(@Valid @RequestBody request: Request): Response {
        return createService(request)
    }
}