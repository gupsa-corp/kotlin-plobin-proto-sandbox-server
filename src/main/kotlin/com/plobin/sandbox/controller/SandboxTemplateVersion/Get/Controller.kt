package com.plobin.sandbox.controller.SandboxTemplateVersion.Get

import com.plobin.sandbox.repository.SandboxTemplateVersionRepository

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun getVersion(id: Long): Response? {
        val version = sandboxTemplateVersionRepository.findById(id).orElse(null)
        return version?.let {
            Response(
                id = it.id,
                sandboxTemplateId = it.sandboxTemplateId,
                versionName = it.versionName,
                versionNumber = it.versionNumber,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}