package com.plobin.sandbox.Repository.SandboxVersion

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("sandboxVersionRepository")
interface Repository : JpaRepository<Entity, Long> {
    fun findBySandboxId(sandboxId: Long): List<Entity>
    fun findByTemplateVersionId(templateVersionId: Long): List<Entity>
    fun findByVersionNameContaining(versionName: String): List<Entity>
    fun findBySandboxIdOrderByCreatedAtDesc(sandboxId: Long): List<Entity>
    fun findBySandboxIdAndIsCurrent(sandboxId: Long, isCurrent: Boolean): Entity?
    fun findBySandboxIdAndVersionName(sandboxId: Long, versionName: String): Entity?
    fun findByIsCurrent(isCurrent: Boolean): List<Entity>
}