package com.plobin.sandbox.controller.SandboxTemplate.Delete

import com.plobin.sandbox.SandboxTemplate.Repository as SandboxTemplateRepository
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class Controller(private val sandboxTemplateRepository: SandboxTemplateRepository) {

    fun deleteTemplate(id: Long): Response? {
        val existingTemplate = sandboxTemplateRepository.findByIdAndDeletedAtIsNull(id)

        return existingTemplate?.let { template ->
            val deletedTemplate = template.copy(
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            sandboxTemplateRepository.save(deletedTemplate)

            Response.fromDeletedEntity(deletedTemplate)
        }
    }
}