package com.plobin.sandbox.controller.SandboxTemplateVersion.Info

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 템플릿 버전 상세 정보")
data class Response(
    @Schema(description = "템플릿 버전 ID", example = "1")
    val id: Long,

    @Schema(description = "템플릿 ID", example = "1")
    val templateId: Long,

    @Schema(description = "버전명", example = "v1.0.0")
    val versionName: String,

    @Schema(description = "버전 번호", example = "1")
    val versionNumber: Int,

    @Schema(description = "템플릿 버전 설명", example = "기본 템플릿 버전")
    val description: String?,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime,

    @Schema(description = "템플릿 기본 정보")
    val templateInfo: TemplateInfo?
) {
    @Schema(description = "템플릿 기본 정보")
    data class TemplateInfo(
        @Schema(description = "템플릿 이름", example = "기본 웹 템플릿")
        val name: String,

        @Schema(description = "템플릿 폴더 경로", example = "Assets/sandbox-templates/web-basic")
        val folderPath: String,

        @Schema(description = "템플릿 설명", example = "웹 개발용 기본 템플릿")
        val description: String?,

        @Schema(description = "템플릿 활성화 여부", example = "true")
        val isActive: Boolean,

        @Schema(description = "전체 템플릿 버전 폴더 경로", example = "Assets/sandbox-templates/web-basic/v1.0.0")
        val fullVersionPath: String
    )

    companion object {
        fun fromEntity(entity: com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity): Response {
            // TODO: Fetch template info from repository if needed
            val templateInfo: TemplateInfo? = null

            return Response(
                id = entity.id,
                templateId = entity.sandboxTemplateId,
                versionName = entity.versionName,
                versionNumber = entity.versionNumber.toInt(),
                description = entity.description,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                templateInfo = templateInfo
            )
        }
    }
}