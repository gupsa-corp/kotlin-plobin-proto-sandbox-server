package com.plobin.sandbox.controller.SandboxTemplate.Update

import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun updateTemplate(id: Long, request: Request): Response? {
        val existingTemplate = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)

        return existingTemplate?.let { template ->
            val updatedTemplate = template.copy(
                sandboxFolderName = request.sandboxFolderName,
                sandboxFolderPath = request.sandboxFolderPath,
                sandboxFullFolderPath = request.sandboxFullFolderPath,
                sandboxStatus = request.sandboxStatus,
                description = request.description,
                isActive = request.isActive ?: template.isActive,
                updatedAt = LocalDateTime.now()
            )

            val savedTemplate = sandboxTemplateRepository.save(updatedTemplate)

            Response.fromEntity(savedTemplate)
        }
    }
}