package com.plobin.sandbox.controller.Sandbox.Update

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxUpdateController")
class Controller(private val sandboxRepository: SandboxRepository) {

    operator fun invoke(id: Long, request: Request): Response {
        val entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)

        val updatedEntity = entity.copy(
            name = request.name ?: entity.name,
            description = request.description ?: entity.description,
            folderPath = request.folderPath ?: entity.folderPath,
            status = request.status ?: entity.status,
            isActive = request.isActive ?: entity.isActive,
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxRepository.save(updatedEntity)
        return Response.fromEntity(savedEntity)
    }
}