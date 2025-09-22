package com.plobin.sandbox.controller.Sandbox.Update

import jakarta.validation.constraints.Size

data class Request(
    @field:Size(max = 255, message = "Name must be less than 255 characters")
    val name: String? = null,

    val description: String? = null,

    @field:Size(max = 1000, message = "Folder path must be less than 1000 characters")
    val folderPath: String? = null,

    @field:Size(max = 50, message = "Status must be less than 50 characters")
    val status: String? = null,

    val isActive: Boolean? = null
)