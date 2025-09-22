package com.plobin.sandbox.controller.SandboxVersion.Create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class Request(
    @field:NotNull(message = "Sandbox ID is required")
    val sandboxId: Long,

    @field:NotNull(message = "Template version ID is required")
    val templateVersionId: Long,

    @field:NotBlank(message = "Version name is required")
    @field:Size(max = 100, message = "Version name must be less than 100 characters")
    val versionName: String,

    @field:NotBlank(message = "Version path is required")
    @field:Size(max = 1000, message = "Version path must be less than 1000 characters")
    val versionPath: String,

    val description: String? = null,

    val isCurrent: Boolean? = false
)