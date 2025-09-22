package com.plobin.sandbox.controller.Sandbox.Delete

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxDeleteController")
class Controller(private val sandboxRepository: SandboxRepository) {

    operator fun invoke(id: Long): Response {
        val entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)

        val deletedEntity = entity.copy(
            deletedAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        sandboxRepository.save(deletedEntity)
        return Response.fromEntity(deletedEntity)
    }
}