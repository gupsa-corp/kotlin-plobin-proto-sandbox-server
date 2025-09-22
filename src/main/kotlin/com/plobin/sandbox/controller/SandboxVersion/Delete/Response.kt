package com.plobin.sandbox.controller.SandboxVersion.Delete

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 버전 삭제 응답")
data class Response(
    @Schema(description = "삭제된 버전 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 ID", example = "1")
    val sandboxId: Long,

    @Schema(description = "버전명", example = "Release")
    val versionName: String,

    @Schema(description = "삭제 처리된 시간")
    val deletedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxVersion.Entity): Response {
            return Response(
                id = entity.id,
                sandboxId = entity.sandboxId,
                versionName = entity.versionName,
                deletedAt = LocalDateTime.now()
            )
        }
    }
}