package com.plobin.sandbox.Repository.SandboxTemplateVersion

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("sandboxTemplateVersionRepository")
interface Repository : JpaRepository<Entity, Long> {
    fun findBySandboxTemplateId(sandboxTemplateId: Long): List<Entity>
    fun findByVersionNameContaining(versionName: String): List<Entity>
    fun findBySandboxTemplateIdOrderByCreatedAtDesc(sandboxTemplateId: Long): List<Entity>
}