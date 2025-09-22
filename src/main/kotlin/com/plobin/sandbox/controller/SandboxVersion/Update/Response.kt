package com.plobin.sandbox.controller.SandboxVersion.Update

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 버전 수정 응답")
data class Response(
    @Schema(description = "수정된 버전 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 ID", example = "1")
    val sandboxId: Long,

    @Schema(description = "버전명", example = "Release")
    val versionName: String,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxVersion.Entity): Response {
            return Response(
                id = entity.id,
                sandboxId = entity.sandboxId,
                versionName = entity.versionName,
                updatedAt = entity.updatedAt
            )
        }
    }
}