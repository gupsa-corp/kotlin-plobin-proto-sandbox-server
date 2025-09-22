package com.plobin.sandbox.controller.SandboxTemplate.Get

import com.plobin.sandbox.repository.SandboxTemplateRepository

class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun getTemplate(id: Long): Response? {
        val template = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)
        return template?.let {
            Response(
                id = it.id,
                sandboxFolderName = it.sandboxFolderName,
                sandboxFolderPath = it.sandboxFolderPath,
                sandboxFullFolderPath = it.sandboxFullFolderPath,
                sandboxStatus = it.sandboxStatus,
                description = it.description,
                isActive = it.isActive,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}