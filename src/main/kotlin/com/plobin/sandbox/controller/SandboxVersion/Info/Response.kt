package com.plobin.sandbox.controller.SandboxVersion.Info

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 버전 상세 정보")
data class Response(
    @Schema(description = "버전 ID", example = "1")
    val id: Long,

    @Schema(description = "샌드박스 ID", example = "1")
    val sandboxId: Long,

    @Schema(description = "템플릿 버전 ID", example = "1")
    val templateVersionId: Long,

    @Schema(description = "버전명", example = "v1.0.0")
    val versionName: String,

    @Schema(description = "버전 폴더 경로", example = "Assets/sandbox-lists/1kld90gji23/v1.0.0")
    val versionPath: String,

    @Schema(description = "버전 설명", example = "첫 번째 버전")
    val description: String?,

    @Schema(description = "현재 버전 여부", example = "true")
    val isCurrent: Boolean,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime,

    @Schema(description = "샌드박스 정보")
    val sandboxInfo: SandboxInfo?,

    @Schema(description = "템플릿 버전 정보")
    val templateVersionInfo: TemplateVersionInfo?
) {
    @Schema(description = "샌드박스 기본 정보")
    data class SandboxInfo(
        @Schema(description = "샌드박스 UUID", example = "1kld90gji23")
        val uuid: String,

        @Schema(description = "샌드박스 이름", example = "내 샌드박스")
        val name: String,

        @Schema(description = "샌드박스 폴더 경로", example = "Assets/sandbox-lists/1kld90gji23")
        val folderPath: String
    )

    @Schema(description = "템플릿 버전 기본 정보")
    data class TemplateVersionInfo(
        @Schema(description = "템플릿 버전명", example = "v1.0.0")
        val versionName: String,

        @Schema(description = "템플릿 버전 설명", example = "기본 템플릿")
        val description: String?
    )

    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxVersion.Entity): Response {
            val sandboxInfo = entity.sandbox?.let { sandbox ->
                SandboxInfo(
                    uuid = sandbox.uuid,
                    name = sandbox.name,
                    folderPath = sandbox.folderPath
                )
            }

            val templateVersionInfo = entity.templateVersion?.let { templateVersion ->
                TemplateVersionInfo(
                    versionName = templateVersion.versionName,
                    description = templateVersion.description
                )
            }

            return Response(
                id = entity.id,
                sandboxId = entity.sandboxId,
                templateVersionId = entity.templateVersionId,
                versionName = entity.versionName,
                versionPath = entity.versionPath,
                description = entity.description,
                isCurrent = entity.isCurrent,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                sandboxInfo = sandboxInfo,
                templateVersionInfo = templateVersionInfo
            )
        }
    }
}