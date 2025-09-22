package com.plobin.sandbox.Repository.SandboxTemplateVersion

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("sandboxTemplateVersionRepository")
interface Repository : JpaRepository<Entity, Long> {
    fun findBySandboxTemplateId(sandboxTemplateId: Long): List<Entity>
    fun findByVersionNameContaining(versionName: String): List<Entity>
    fun findBySandboxTemplateIdOrderByCreatedAtDesc(sandboxTemplateId: Long): List<Entity>

    // 최신 버전 조회 (Serve API용)
    fun findTopBySandboxTemplateIdOrderByCreatedAtDesc(sandboxTemplateId: Long): Entity?

    // 업로드 서비스를 위한 추가 메서드
    fun findBySandboxTemplateIdAndVersionNumber(sandboxTemplateId: Long, versionNumber: String): Entity?
}