package com.plobin.sandbox.controller.SandboxVersion.Create

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Repository.SandboxVersion.Entity
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxVersionCreateController")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    operator fun invoke(request: Request): Response {
        val entity = Entity(
            sandboxId = request.sandboxId,
            templateVersionId = request.templateVersionId,
            versionName = request.versionName,
            versionPath = request.versionPath,
            description = request.description,
            isCurrent = request.isCurrent ?: false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxVersionRepository.save(entity)
        return Response.fromEntity(savedEntity)
    }
}