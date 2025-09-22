package com.plobin.sandbox.util.DataSeeding.SeedSandboxes

import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Repository.Sandbox.Entity as Sandbox
import com.plobin.sandbox.Repository.SandboxVersion.Entity as SandboxVersion
import com.plobin.sandbox.util.DataSeeding.ScanSandboxFolders.Util as ScanSandboxFoldersUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component("seedSandboxesUtil")
class Util(
    private val sandboxTemplateRepository: SandboxTemplateRepository,
    private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository,
    private val sandboxRepository: SandboxRepository,
    private val sandboxVersionRepository: SandboxVersionRepository,
    private val scanSandboxFoldersUtil: ScanSandboxFoldersUtil
) {

    /**
     * 샌드박스 시딩
     */
    operator fun invoke() {
        println("Seeding sandboxes...")

        val sandboxFolders = scanSandboxFoldersUtil()

        for (sandboxFolder in sandboxFolders) {
            // 기존 샌드박스 확인
            val existingSandbox = sandboxRepository.findByUuidAndDeletedAtIsNull(sandboxFolder.uuid)

            val sandbox = existingSandbox ?: run {
                // 기본 템플릿 찾기 (첫 번째 템플릿 사용)
                val defaultTemplate = sandboxTemplateRepository.findByDeletedAtIsNull().firstOrNull()
                    ?: throw RuntimeException("No template found for sandbox seeding")

                // 새 샌드박스 생성
                val newSandbox = Sandbox(
                    uuid = sandboxFolder.uuid,
                    name = "${sandboxFolder.uuid} 샌드박스",
                    description = "${sandboxFolder.uuid} 샌드박스 인스턴스",
                    folderPath = sandboxFolder.path,
                    templateId = defaultTemplate.id,
                    status = "active",
                    isActive = true,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                val saved = sandboxRepository.save(newSandbox)
                println("Created sandbox: ${sandboxFolder.uuid}")
                saved
            }

            // 샌드박스 버전들 시딩
            for (version in sandboxFolder.versions) {
                val existingVersion = sandboxVersionRepository
                    .findBySandboxIdAndVersionName(sandbox.id, version.name)

                if (existingVersion == null) {
                    // 기본 템플릿 버전 찾기
                    val defaultTemplateVersion = sandboxTemplateVersionRepository
                        .findBySandboxTemplateId(sandbox.templateId).firstOrNull()
                        ?: throw RuntimeException("No template version found for sandbox version seeding")

                    val newVersion = SandboxVersion(
                        sandboxId = sandbox.id,
                        templateVersionId = defaultTemplateVersion.id,
                        versionName = version.name,
                        versionPath = version.path,
                        description = "${version.name} 버전",
                        isCurrent = version.isCurrent,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    sandboxVersionRepository.save(newVersion)
                    println("Created sandbox version: ${sandbox.uuid}/${version.name}")
                }
            }
        }
    }
}