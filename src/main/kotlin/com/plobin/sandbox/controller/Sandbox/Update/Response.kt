package com.plobin.sandbox.controller.Sandbox.Update

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 수정 응답")
data class Response(
    @Schema(description = "수정된 샌드박스 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 UUID", example = "1kld90gji23")
    val uuid: String,

    @Schema(description = "샌드박스 이름", example = "내 샌드박스")
    val name: String,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.Sandbox.Entity): Response {
            return Response(
                id = entity.id,
                uuid = entity.uuid,
                name = entity.name,
                updatedAt = entity.updatedAt
            )
        }
    }
}