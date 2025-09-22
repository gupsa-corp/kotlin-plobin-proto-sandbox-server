package com.plobin.sandbox.controller.SandboxVersion.Update

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Exception.SandboxVersion.Exception as SandboxVersionException
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController("sandboxVersionUpdateController")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    operator fun invoke(id: Long, request: Request): Response {
        val entity = sandboxVersionRepository.findById(id).orElse(null)
            ?: throw SandboxVersionException.notFound(id)

        val updatedEntity = entity.copy(
            versionName = request.versionName ?: entity.versionName,
            versionPath = request.versionPath ?: entity.versionPath,
            description = request.description ?: entity.description,
            isCurrent = request.isCurrent ?: entity.isCurrent,
            updatedAt = LocalDateTime.now()
        )

        val savedEntity = sandboxVersionRepository.save(updatedEntity)
        return Response.fromEntity(savedEntity)
    }
}