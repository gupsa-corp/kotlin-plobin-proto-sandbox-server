package com.plobin.sandbox.controller.SandboxTemplate.Serve

import java.time.LocalDateTime

data class Response(
    val templateId: Long,
    val templateName: String,
    val versionId: Long,
    val versionName: String,
    val versionNumber: String,
    val downloadUrl: String,
    val fileSize: Long,
    val contentType: String = "application/zip",
    val servedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun fromEntities(
            template: com.plobin.sandbox.Repository.SandboxTemplate.Entity,
            version: com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity,
            downloadUrl: String,
            fileSize: Long
        ): Response {
            return Response(
                templateId = template.id,
                templateName = template.sandboxFolderName,
                versionId = version.id,
                versionName = version.versionName,
                versionNumber = version.versionNumber,
                downloadUrl = downloadUrl,
                fileSize = fileSize
            )
        }
    }
}