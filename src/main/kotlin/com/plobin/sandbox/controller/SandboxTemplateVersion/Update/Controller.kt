package com.plobin.sandbox.controller.SandboxTemplateVersion.Update

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxTemplateVersionUpdateController")
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

            Response.fromEntity(savedVersion)
        }
    }
}