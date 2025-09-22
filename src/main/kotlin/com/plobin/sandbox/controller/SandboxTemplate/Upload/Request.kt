package com.plobin.sandbox.controller.SandboxTemplate.Upload

import org.springframework.web.multipart.MultipartFile
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class Request(
    @field:NotBlank(message = "Template name is required")
    @field:Size(max = 255, message = "Template name must be less than 255 characters")
    val templateName: String,

    @field:Size(max = 1000, message = "Description must be less than 1000 characters")
    val description: String? = null,

    @field:NotEmpty(message = "At least one file is required")
    val files: List<MultipartFile>,

    val isRelease: Boolean = false
)