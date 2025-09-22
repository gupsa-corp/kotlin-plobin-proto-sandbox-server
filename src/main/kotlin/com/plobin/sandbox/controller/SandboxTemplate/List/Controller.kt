package com.plobin.sandbox.controller.SandboxTemplate.List

import com.plobin.sandbox.Config.Swagger.Annotations.Operations
import com.plobin.sandbox.Config.Swagger.Annotations.CommonResponses
import com.plobin.sandbox.Config.Swagger.Annotations.Tags
import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sandbox-templates")
@get:Tags.SandboxTemplate
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    @get:Operations.SandboxTemplateList
    @get:CommonResponses.StandardSuccess
    @GetMapping
    fun listTemplates(): List<Response> {
        val templates = sandboxTemplateRepository.findByDeletedAtIsNull()
        return templates.map { Response.fromEntity(it) }
    }
}