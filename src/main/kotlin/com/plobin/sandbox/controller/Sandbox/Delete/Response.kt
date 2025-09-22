package com.plobin.sandbox.controller.Sandbox.Delete

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 삭제 응답")
data class Response(
    @Schema(description = "삭제된 샌드박스 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 UUID", example = "1kld90gji23")
    val uuid: String,

    @Schema(description = "삭제 처리된 시간")
    val deletedAt: LocalDateTime?
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.Sandbox.Entity): Response {
            return Response(
                id = entity.id,
                uuid = entity.uuid,
                deletedAt = entity.deletedAt
            )
        }
    }
}