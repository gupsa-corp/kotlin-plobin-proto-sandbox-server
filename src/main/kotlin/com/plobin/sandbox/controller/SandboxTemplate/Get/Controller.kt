package com.plobin.sandbox.controller.SandboxTemplate.Get

import com.plobin.sandbox.Repository.SandboxTemplate.Entity
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Exception.SandboxTemplate.Exception as SandboxTemplateException
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxTemplateGetController")
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun getTemplate(id: Long): Response {
        val template: Entity = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxTemplateException.notFound(id)
        return Response.fromEntity(template)
    }
}