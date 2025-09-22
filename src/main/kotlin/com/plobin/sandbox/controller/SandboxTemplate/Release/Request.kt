package com.plobin.sandbox.controller.SandboxTemplate.Release

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class Request(
    @field:NotNull(message = "Version ID is required")
    @field:Positive(message = "Version ID must be positive")
    val versionId: Long
)