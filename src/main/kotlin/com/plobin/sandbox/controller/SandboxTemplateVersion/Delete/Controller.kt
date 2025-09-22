package com.plobin.sandbox.controller.SandboxTemplateVersion.Delete

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository

class Controller(private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository) {

    fun deleteVersion(id: Long): Response? {
        val existingVersion = sandboxTemplateVersionRepository.findById(id).orElse(null)

        return existingVersion?.let { version ->
            sandboxTemplateVersionRepository.deleteById(id)

            Response(
                id = version.id,
                message = "Version successfully deleted",
                versionName = version.versionName,
                versionNumber = version.versionNumber
            )
        }
    }
}