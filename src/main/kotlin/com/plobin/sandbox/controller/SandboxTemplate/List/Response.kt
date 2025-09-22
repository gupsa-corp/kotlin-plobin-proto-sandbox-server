package com.plobin.sandbox.controller.SandboxTemplate.List

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 템플릿 정보")
data class Response(
    @Schema(description = "템플릿 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 폴더명", example = "template-folder")
    val sandboxFolderName: String,

    @Schema(description = "샌드박스 폴더 경로", example = "/path/to/template")
    val sandboxFolderPath: String,

    @Schema(description = "샌드박스 전체 폴더 경로", example = "/full/path/to/template")
    val sandboxFullFolderPath: String,

    @Schema(description = "샌드박스 상태", example = "active")
    val sandboxStatus: String,

    @Schema(description = "템플릿 설명", example = "기본 템플릿")
    val description: String?,

    @Schema(description = "활성화 여부", example = "true")
    val isActive: Boolean,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxTemplate.Entity): Response {
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