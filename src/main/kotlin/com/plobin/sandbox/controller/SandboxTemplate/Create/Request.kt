package com.plobin.sandbox.controller.SandboxTemplate.Create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Request(
    @field:NotBlank(message = "Sandbox folder name is required")
    @field:Size(max = 255, message = "Sandbox folder name must be less than 255 characters")
    val sandboxFolderName: String,

    @field:NotBlank(message = "Sandbox folder path is required")
    @field:Size(max = 500, message = "Sandbox folder path must be less than 500 characters")
    val sandboxFolderPath: String,

    @field:NotBlank(message = "Sandbox full folder path is required")
    @field:Size(max = 1000, message = "Sandbox full folder path must be less than 1000 characters")
    val sandboxFullFolderPath: String,

    @field:NotBlank(message = "Sandbox status is required")
    @field:Size(max = 50, message = "Sandbox status must be less than 50 characters")
    val sandboxStatus: String,

    val description: String? = null,

    val isActive: Boolean? = true
)