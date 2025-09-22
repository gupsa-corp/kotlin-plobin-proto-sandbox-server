package com.plobin.sandbox.controller.SandboxTemplate.List

import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository

class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun listTemplates(): List<Response> {
        val templates = sandboxTemplateRepository.findByDeletedAtIsNull()
        return templates.map { Response.fromEntity(it) }
    }
}