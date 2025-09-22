package com.plobin.sandbox.controller.SandboxTemplateVersion.List

import com.plobin.sandbox.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun listVersions(): List<Response> {
        val versions = sandboxTemplateVersionRepository.findAll()
        return versions.map { Response.fromEntity(it) }
    }
}