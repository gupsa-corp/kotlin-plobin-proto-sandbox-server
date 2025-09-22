package com.plobin.sandbox.controller.SandboxTemplateVersion.List

import com.plobin.sandbox.repository.SandboxTemplateVersionRepository

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun listVersions(): List<Response> {
        val versions = sandboxTemplateVersionRepository.findAll()
        return versions.map { version ->
            Response(
                id = version.id,
                sandboxTemplateId = version.sandboxTemplateId,
                versionName = version.versionName,
                versionNumber = version.versionNumber,
                description = version.description,
                createdAt = version.createdAt,
                updatedAt = version.updatedAt
            )
        }
    }
}