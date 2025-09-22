package com.plobin.sandbox.controller.SandboxVersion.List

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 버전 정보")
data class Response(
    @Schema(description = "버전 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 ID", example = "1")
    val sandboxId: Long,

    @Schema(description = "템플릿 버전 ID", example = "1")
    val templateVersionId: Long,

    @Schema(description = "버전명", example = "Release")
    val versionName: String,

    @Schema(description = "버전 경로", example = "Assets/sandbox-lists/1kld90gji23/Release")
    val versionPath: String,

    @Schema(description = "버전 설명", example = "릴리즈 버전")
    val description: String?,

    @Schema(description = "현재 활성 버전 여부", example = "true")
    val isCurrent: Boolean,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxVersion.Entity): Response {
            return Response(
                id = entity.id,
                sandboxId = entity.sandboxId,
                templateVersionId = entity.templateVersionId,
                versionName = entity.versionName,
                versionPath = entity.versionPath,
                description = entity.description,
                isCurrent = entity.isCurrent,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}