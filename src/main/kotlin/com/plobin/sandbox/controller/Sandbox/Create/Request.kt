package com.plobin.sandbox.controller.Sandbox.Create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class Request(
    @field:NotBlank(message = "UUID is required")
    @field:Size(max = 255, message = "UUID must be less than 255 characters")
    val uuid: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(max = 255, message = "Name must be less than 255 characters")
    val name: String,

    val description: String? = null,

    @field:NotBlank(message = "Folder path is required")
    @field:Size(max = 1000, message = "Folder path must be less than 1000 characters")
    val folderPath: String,

    @field:NotNull(message = "Template ID is required")
    val templateId: Long,

    @field:NotBlank(message = "Status is required")
    @field:Size(max = 50, message = "Status must be less than 50 characters")
    val status: String = "active",

    val createdBy: Long? = null,

    val isActive: Boolean? = true
)