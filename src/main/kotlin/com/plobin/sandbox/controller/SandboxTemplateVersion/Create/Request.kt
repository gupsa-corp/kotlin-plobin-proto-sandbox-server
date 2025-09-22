package com.plobin.sandbox.controller.SandboxTemplateVersion.Create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class Request(
    @field:NotNull(message = "Sandbox template ID is required")
    @field:Positive(message = "Sandbox template ID must be positive")
    val sandboxTemplateId: Long,

    @field:NotBlank(message = "Version name is required")
    @field:Size(max = 100, message = "Version name must be less than 100 characters")
    val versionName: String,

    @field:NotBlank(message = "Version number is required")
    @field:Size(max = 50, message = "Version number must be less than 50 characters")
    val versionNumber: String,

    val description: String? = null
)