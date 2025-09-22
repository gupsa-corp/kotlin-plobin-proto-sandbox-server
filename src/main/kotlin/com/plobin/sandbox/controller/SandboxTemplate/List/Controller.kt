package com.plobin.sandbox.controller.SandboxTemplate.List

import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository

class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun listTemplates(): List<Response> {
        val templates = sandboxTemplateRepository.findByDeletedAtIsNull()
        return templates.map { template ->
            Response(
                id = template.id,
                sandboxFolderName = template.sandboxFolderName,
                sandboxFolderPath = template.sandboxFolderPath,
                sandboxFullFolderPath = template.sandboxFullFolderPath,
                sandboxStatus = template.sandboxStatus,
                description = template.description,
                isActive = template.isActive,
                createdAt = template.createdAt,
                updatedAt = template.updatedAt
            )
        }
    }
}