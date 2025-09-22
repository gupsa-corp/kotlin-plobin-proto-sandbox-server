package com.plobin.sandbox.controller.Sandbox.Create

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.Sandbox.Entity
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxCreateController")
class Controller(private val sandboxRepository: SandboxRepository) {

    operator fun invoke(request: Request): Response {
        val entity = Entity(
            uuid = request.uuid,
            name = request.name,
            description = request.description,
            folderPath = request.folderPath,
            templateId = request.templateId,
            status = request.status,
            createdBy = request.createdBy,
            isActive = request.isActive ?: true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxRepository.save(entity)
        return Response.fromEntity(savedEntity)
    }
}