package com.plobin.sandbox.util.DataSeeding.SeedTemplates

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplate
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersion
import com.plobin.sandbox.util.DataSeeding.ScanTemplatesFolders.Util as ScanTemplatesFoldersUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component("seedTemplatesUtil")
class Util(
    private val sandboxTemplateRepository: SandboxTemplateRepository,
    private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository,
    private val scanTemplatesFoldersUtil: ScanTemplatesFoldersUtil
) {

    /**
     * 템플릿 시딩
     */
    operator fun invoke() {
        println("Seeding templates...")

        val templateFolders = scanTemplatesFoldersUtil()

        for (templateFolder in templateFolders) {
            // 기존 템플릿 확인
            val existingTemplate = sandboxTemplateRepository
                .findBySandboxFolderNameAndIsActiveTrue(templateFolder.name)

            val template = existingTemplate ?: run {
                // 새 템플릿 생성
                val newTemplate = SandboxTemplate(
                    sandboxFolderName = templateFolder.name,
                    sandboxFolderPath = templateFolder.path,
                    sandboxFullFolderPath = templateFolder.fullPath,
                    sandboxStatus = "ACTIVE",
                    description = "${templateFolder.name} 템플릿",
                    isActive = true,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                val saved = sandboxTemplateRepository.save(newTemplate)
                println("Created template: ${templateFolder.name}")
                saved
            }

            // 템플릿 버전들 시딩
            for (version in templateFolder.versions) {
                val existingVersion = sandboxTemplateVersionRepository
                    .findBySandboxTemplateIdAndVersionNumber(template.id, version.name)

                if (existingVersion == null) {
                    val newVersion = SandboxTemplateVersion(
                        sandboxTemplateId = template.id,
                        versionName = if (version.isRelease) "Release" else "Version ${version.name}",
                        versionNumber = version.name,
                        description = "${version.name} 버전",
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    sandboxTemplateVersionRepository.save(newVersion)
                    println("Created template version: ${template.sandboxFolderName}/${version.name}")
                }
            }
        }
    }
}