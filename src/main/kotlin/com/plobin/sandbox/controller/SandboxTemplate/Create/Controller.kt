package com.plobin.sandbox.controller.SandboxTemplate.Create

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplate
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import java.time.LocalDateTime

class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun createTemplate(request: Request): Response {
        val template = SandboxTemplate(
            sandboxFolderName = request.sandboxFolderName,
            sandboxFolderPath = request.sandboxFolderPath,
            sandboxFullFolderPath = request.sandboxFullFolderPath,
            sandboxStatus = request.sandboxStatus,
            description = request.description,
            isActive = request.isActive ?: true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedTemplate = sandboxTemplateRepository.save(template)

        return Response(
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