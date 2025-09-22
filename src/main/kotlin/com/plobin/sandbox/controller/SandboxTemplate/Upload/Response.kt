package com.plobin.sandbox.controller.SandboxTemplate.Upload

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplate
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersion

data class Response(
    val templateId: Long,
    val versionId: Long,
    val templateName: String,
    val versionNumber: String,
    val versionName: String,
    val uploadedFiles: List<String>,
    val fullFolderPath: String,
    val isRelease: Boolean,
    val message: String
) {
    companion object {
        fun fromEntities(
            template: SandboxTemplate,
            version: SandboxTemplateVersion,
            uploadedFiles: List<String>,
            fullFolderPath: String,
            isRelease: Boolean = false
        ): Response {
            return Response(
                templateId = template.id,
                versionId = version.id,
                templateName = template.sandboxFolderName,
                versionNumber = version.versionNumber,
                versionName = version.versionName,
                uploadedFiles = uploadedFiles,
                fullFolderPath = fullFolderPath,
                isRelease = isRelease,
                message = if (isRelease) {
                    "Template uploaded successfully and set as Release"
                } else {
                    "Template uploaded successfully as version ${version.versionNumber}"
                }
            )
        }
    }
}