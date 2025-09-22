package com.plobin.sandbox.controller.SandboxVersion.Update

import jakarta.validation.constraints.Size

data class Request(
    @field:Size(max = 100, message = "Version name must be less than 100 characters")
    val versionName: String? = null,

    @field:Size(max = 1000, message = "Version path must be less than 1000 characters")
    val versionPath: String? = null,

    val description: String? = null,

    val isCurrent: Boolean? = null
)