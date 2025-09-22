package com.plobin.sandbox.controller.SandboxTemplate.Upload

import com.plobin.sandbox.service.SandboxTemplate.Upload.Service as UploadService
import com.plobin.sandbox.Config.Swagger.Annotations.Tags
import com.plobin.sandbox.Config.Swagger.Annotations.CommonResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

@RestController("sandboxTemplateUploadController")
@RequestMapping("/api/sandbox-templates")
@Validated
// @Tags.SandboxTemplate
class Controller(private val uploadService: UploadService) {

    @PostMapping(
        "/upload",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Upload sandbox template files",
        description = """
            Upload files to create a new sandbox template version.
            Supports both individual files and ZIP archives.
            If isRelease is true, the uploaded version will be set as the Release version.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Template uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Response::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters or file validation failed"
            ),
            ApiResponse(
                responseCode = "413",
                description = "File size exceeds the maximum allowed limit"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error during file processing"
            )
        ]
    )
    // @CommonResponses.BadRequest
    // @CommonResponses.InternalServerError
    fun uploadTemplate(
        @RequestParam("templateName")
        @NotBlank(message = "Template name is required")
        templateName: String,

        @RequestParam("description", required = false)
        description: String?,

        @RequestParam("files")
        @NotEmpty(message = "At least one file is required")
        files: List<MultipartFile>,

        @RequestParam("isRelease", defaultValue = "false")
        isRelease: Boolean
    ): ResponseEntity<Response> {

        val request = Request(
            templateName = templateName.trim(),
            description = description?.trim(),
            files = files,
            isRelease = isRelease
        )

        val response = uploadService(request)
        return ResponseEntity.ok(response)
    }
}