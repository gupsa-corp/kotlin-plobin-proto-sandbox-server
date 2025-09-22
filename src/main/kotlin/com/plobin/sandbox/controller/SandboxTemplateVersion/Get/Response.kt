package com.plobin.sandbox.controller.SandboxTemplateVersion.Get

import java.time.LocalDateTime

data class Response(
    val id: Long,
    val sandboxTemplateId: Long,
    val versionName: String,
    val versionNumber: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)