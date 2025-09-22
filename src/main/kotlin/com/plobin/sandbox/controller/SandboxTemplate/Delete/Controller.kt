package com.plobin.sandbox.controller.SandboxTemplate.Delete

import com.plobin.sandbox.repository.SandboxTemplateRepository
import java.time.LocalDateTime

class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun deleteTemplate(id: Long): Response? {
        val existingTemplate = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)

        return existingTemplate?.let { template ->
            val deletedTemplate = template.copy(
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            sandboxTemplateRepository.save(deletedTemplate)

            Response(
                id = deletedTemplate.id,
                message = "Template successfully deleted",
                deletedAt = deletedTemplate.deletedAt!!
            )
        }
    }
}