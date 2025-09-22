package com.plobin.sandbox.controller.SandboxTemplateVersion.Update

import com.plobin.sandbox.repository.SandboxTemplateVersionRepository
import java.time.LocalDateTime

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun updateVersion(id: Long, request: Request): Response? {
        val existingVersion = sandboxTemplateVersionRepository.findById(id).orElse(null)

        return existingVersion?.let { version ->
            val updatedVersion = version.copy(
                versionName = request.versionName,
                versionNumber = request.versionNumber,
                description = request.description,
                updatedAt = LocalDateTime.now()
            )

            val savedVersion = sandboxTemplateVersionRepository.save(updatedVersion)

            Response(
                id = savedVersion.id,
                sandboxTemplateId = savedVersion.sandboxTemplateId,
                versionName = savedVersion.versionName,
                versionNumber = savedVersion.versionNumber,
                description = savedVersion.description,
                createdAt = savedVersion.createdAt,
                updatedAt = savedVersion.updatedAt
            )
        }
    }
}