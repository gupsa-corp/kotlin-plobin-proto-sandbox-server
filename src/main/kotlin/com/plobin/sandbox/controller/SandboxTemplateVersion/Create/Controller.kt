package com.plobin.sandbox.controller.SandboxTemplateVersion.Create

import com.plobin.sandbox.entity.SandboxTemplateVersion
import com.plobin.sandbox.repository.SandboxTemplateVersionRepository
import java.time.LocalDateTime

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun createVersion(request: Request): Response {
        val version = SandboxTemplateVersion(
            sandboxTemplateId = request.sandboxTemplateId,
            versionName = request.versionName,
            versionNumber = request.versionNumber,
            description = request.description,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedVersion = sandboxTemplateVersionRepository.save(version)

        return Response(
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