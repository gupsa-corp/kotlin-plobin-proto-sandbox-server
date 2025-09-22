package com.plobin.sandbox.controller.SandboxTemplateVersion.List

import java.time.LocalDateTime

data class Response(
    val id: Long,
    val sandboxTemplateId: Long,
    val versionName: String,
    val versionNumber: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.SandboxTemplateVersion.Entity): Response {
            return Response(
                id = entity.id,
                sandboxTemplateId = entity.sandboxTemplateId,
                versionName = entity.versionName,
                versionNumber = entity.versionNumber,
                description = entity.description,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}