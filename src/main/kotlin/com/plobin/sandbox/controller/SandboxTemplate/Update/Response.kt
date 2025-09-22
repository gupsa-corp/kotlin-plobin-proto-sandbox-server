package com.plobin.sandbox.controller.SandboxTemplate.Update

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
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.SandboxTemplate.Entity): Response {
            return Response(
                id = entity.id,
                sandboxFolderName = entity.sandboxFolderName,
                sandboxFolderPath = entity.sandboxFolderPath,
                sandboxFullFolderPath = entity.sandboxFullFolderPath,
                sandboxStatus = entity.sandboxStatus,
                description = entity.description,
                isActive = entity.isActive,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}