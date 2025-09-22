package com.plobin.sandbox.controller.SandboxTemplate.Update

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import java.time.LocalDateTime

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

            Response(
                id = savedTemplate.id,
                sandboxFolderName = savedTemplate.sandboxFolderName,
                sandboxFolderPath = savedTemplate.sandboxFolderPath,
                sandboxFullFolderPath = savedTemplate.sandboxFullFolderPath,
                sandboxStatus = savedTemplate.sandboxStatus,
                description = savedTemplate.description,
                isActive = savedTemplate.isActive,
                createdAt = savedTemplate.createdAt,
                updatedAt = savedTemplate.updatedAt
            )
        }
    }
}