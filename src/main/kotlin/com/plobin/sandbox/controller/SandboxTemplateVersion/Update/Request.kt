package com.plobin.sandbox.controller.SandboxTemplateVersion.Update

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Request(
    @field:NotBlank(message = "Version name is required")
    @field:Size(max = 100, message = "Version name must be less than 100 characters")
    val versionName: String,

    @field:NotBlank(message = "Version number is required")
    @field:Size(max = 50, message = "Version number must be less than 50 characters")
    val versionNumber: String,

    val description: String? = null
)