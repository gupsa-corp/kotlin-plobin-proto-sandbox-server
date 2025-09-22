package com.plobin.sandbox.controller.Sandbox.Get

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 버전 정보")
data class VersionInfo(
    @Schema(description = "버전 ID", example = "1")
    val id: Long,

    @Schema(description = "버전명", example = "v1.0.0")
    val versionName: String,

    @Schema(description = "버전 경로", example = "Assets/sandbox-lists/1kld90gji23/v1.0.0")
    val versionPath: String,

    @Schema(description = "버전 설명", example = "첫 번째 버전")
    val description: String?,

    @Schema(description = "현재 버전 여부", example = "true")
    val isCurrent: Boolean,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime
)

@Schema(description = "샌드박스 상세 정보")
data class Response(
    @Schema(description = "샌드박스 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 UUID", example = "1kld90gji23")
    val uuid: String,

    @Schema(description = "샌드박스 이름", example = "내 샌드박스")
    val name: String,

    @Schema(description = "샌드박스 설명", example = "테스트용 샌드박스")
    val description: String?,

    @Schema(description = "폴더 경로", example = "Assets/sandbox-lists/1kld90gji23")
    val folderPath: String,

    @Schema(description = "템플릿 ID", example = "1")
    val templateId: Long,

    @Schema(description = "샌드박스 상태", example = "active")
    val status: String,

    @Schema(description = "생성자 ID", example = "1")
    val createdBy: Long?,

    @Schema(description = "활성화 여부", example = "true")
    val isActive: Boolean,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime,

    @Schema(description = "샌드박스 버전 목록")
    val versions: List<VersionInfo>
) {
    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.Sandbox.Entity): Response {
            val versions = entity.versions.map { version ->
                VersionInfo(
                    id = version.id,
                    versionName = version.versionName,
                    versionPath = version.versionPath,
                    description = version.description,
                    isCurrent = version.isCurrent,
                    createdAt = version.createdAt
                )
            }

            return Response(
                id = entity.id,
                uuid = entity.uuid,
                name = entity.name,
                description = entity.description,
                folderPath = entity.folderPath,
                templateId = entity.templateId,
                status = entity.status,
                createdBy = entity.createdBy,
                isActive = entity.isActive,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                versions = versions
            )
        }
    }
}