package com.plobin.sandbox.controller.SandboxTemplateVersion.Create

import com.plobin.sandbox.SandboxTemplateVersion.Entity as SandboxTemplateVersion
import com.plobin.sandbox.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
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

        return Response.fromEntity(savedVersion)
    }
}