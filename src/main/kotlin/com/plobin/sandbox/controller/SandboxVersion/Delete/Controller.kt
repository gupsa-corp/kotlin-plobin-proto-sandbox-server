package com.plobin.sandbox.controller.SandboxVersion.Delete

import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Exception.SandboxVersion.Exception as SandboxVersionException
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxVersionDeleteController")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    operator fun invoke(id: Long): Response {
        val entity = sandboxVersionRepository.findById(id).orElse(null)
            ?: throw SandboxVersionException.notFound(id)

        sandboxVersionRepository.delete(entity)
        return Response.fromEntity(entity)
    }
}