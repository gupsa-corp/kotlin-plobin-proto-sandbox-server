package com.plobin.sandbox.controller.SandboxTemplateVersion.Get

import com.plobin.sandbox.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun getVersion(id: Long): Response? {
        val version = sandboxTemplateVersionRepository.findById(id).orElse(null)
        return version?.let { Response.fromEntity(it) }
    }
}