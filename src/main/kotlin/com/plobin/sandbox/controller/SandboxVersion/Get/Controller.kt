package com.plobin.sandbox.controller.SandboxVersion.Get

import com.plobin.sandbox.Repository.SandboxVersion.Entity
import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Exception.SandboxVersion.Exception as SandboxVersionException
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxVersionGetController")
class Controller(private val sandboxVersionRepository: SandboxVersionRepository) {

    operator fun invoke(id: Long): Response {
        val version: Entity = sandboxVersionRepository.findById(id).orElse(null)
            ?: throw SandboxVersionException.notFound(id)
        return Response.fromEntity(version)
    }
}