package com.plobin.sandbox.controller.SandboxTemplate.Create

import java.time.LocalDateTime

data class Response(
    val id: Long,
    val sandboxFolderName: String,
    val sandboxFolderPath: String,
    val sandboxFullFolderPath: String,
    val sandboxStatus: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)