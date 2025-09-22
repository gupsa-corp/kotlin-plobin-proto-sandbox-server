package com.plobin.sandbox.controller.SandboxTemplate.Get

import com.plobin.sandbox.SandboxTemplate.Entity
import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun getTemplate(id: Long): Response? {
        val template: Entity = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)
        return Response.fromEntity(template)
    }
}