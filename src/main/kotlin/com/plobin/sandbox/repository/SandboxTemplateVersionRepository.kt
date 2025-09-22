package com.plobin.sandbox.repository

import com.plobin.sandbox.entity.SandboxTemplateVersion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SandboxTemplateVersionRepository : JpaRepository<SandboxTemplateVersion, Long> {
    fun findBySandboxTemplateId(sandboxTemplateId: Long): List<SandboxTemplateVersion>
    fun findByVersionNameContaining(versionName: String): List<SandboxTemplateVersion>
    fun findBySandboxTemplateIdOrderByCreatedAtDesc(sandboxTemplateId: Long): List<SandboxTemplateVersion>
}