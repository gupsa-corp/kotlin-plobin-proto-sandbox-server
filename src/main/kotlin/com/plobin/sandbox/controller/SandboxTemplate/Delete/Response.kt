package com.plobin.sandbox.controller.SandboxTemplate.Delete

import java.time.LocalDateTime

data class Response(
    val id: Long,
    val message: String,
    val deletedAt: LocalDateTime
) {
    companion object {
        fun fromDeletedEntity(entity: com.plobin.sandbox.Repository.SandboxTemplate.Entity, message: String = "Template successfully deleted"): Response {
            return Response(
                id = entity.id,
                message = message,
                deletedAt = entity.deletedAt!!
            )
        }
    }
}