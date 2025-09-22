package com.plobin.sandbox.service.SandboxTemplate.Create

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplate
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.controller.SandboxTemplate.Create.Request
import com.plobin.sandbox.controller.SandboxTemplate.Create.Response
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service("sandboxTemplateCreateService")
class Service(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    operator fun invoke(request: Request): Response {
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

        return Response.fromEntity(savedTemplate)
    }
}