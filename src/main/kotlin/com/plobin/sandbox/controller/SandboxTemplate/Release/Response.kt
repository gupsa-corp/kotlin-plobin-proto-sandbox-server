package com.plobin.sandbox.controller.SandboxTemplate.Release

import java.time.LocalDateTime

data class Response(
    val templateId: Long,
    val templateName: String,
    val releasedVersionId: Long,
    val releasedVersionName: String,
    val releasedVersionNumber: String,
    val releasedAt: LocalDateTime,
    val message: String = "Template version has been successfully set as release"
) {
    companion object {
        fun fromEntities(
            template: com.plobin.sandbox.Repository.SandboxTemplate.Entity,
            version: com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity
        ): Response {
            return Response(
                templateId = template.id,
                templateName = template.sandboxFolderName,
                releasedVersionId = version.id,
                releasedVersionName = version.versionName,
                releasedVersionNumber = version.versionNumber,
                releasedAt = LocalDateTime.now()
            )
        }
    }
}