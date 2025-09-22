package com.plobin.sandbox.controller.SandboxTemplate.Delete

import java.time.LocalDateTime

data class Response(
    val id: Long,
    val message: String,
    val deletedAt: LocalDateTime
)