package com.plobin.sandbox.controller.Sandbox.ReleaseInfo

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "샌드박스 Release 버전 정보")
data class Response(
    @Schema(description = "샌드박스 ID", example = "1")
    val sandboxId: Long,

    @Schema(description = "샌드박스 이름", example = "내 샌드박스")
    val sandboxName: String,

    @Schema(description = "샌드박스 폴더 경로", example = "Assets/sandbox-lists/1kld90gji23")
    val sandboxFolderPath: String,

    @Schema(description = "현재 Release 버전 ID", example = "1")
    val releaseVersionId: Long?,

    @Schema(description = "현재 Release 버전명", example = "v1.0.0")
    val releaseVersionName: String?,

    @Schema(description = "Release 버전 경로", example = "Assets/sandbox-lists/1kld90gji23/Release")
    val releaseVersionPath: String?,

    @Schema(description = "Release 버전 설명", example = "프로덕션 배포 버전")
    val releaseDescription: String?,

    @Schema(description = "Release 설정일시")
    val releaseSetAt: LocalDateTime?
) {
    companion object {
        fun fromSandboxAndVersion(
            sandbox: com.plobin.sandbox.Repository.Sandbox.Entity,
            releaseVersion: com.plobin.sandbox.Repository.SandboxVersion.Entity?
        ): Response {
            return Response(
                sandboxId = sandbox.id,
                sandboxName = sandbox.name,
                sandboxFolderPath = sandbox.folderPath,
                releaseVersionId = releaseVersion?.id,
                releaseVersionName = releaseVersion?.versionName,
                releaseVersionPath = releaseVersion?.let { "${sandbox.folderPath}/Release" },
                releaseDescription = releaseVersion?.description,
                releaseSetAt = releaseVersion?.updatedAt
            )
        }
    }
}