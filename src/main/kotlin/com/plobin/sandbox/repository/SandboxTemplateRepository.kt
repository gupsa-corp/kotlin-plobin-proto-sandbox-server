package com.plobin.sandbox.repository

import com.plobin.sandbox.entity.SandboxTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SandboxTemplateRepository : JpaRepository<SandboxTemplate, Long> {
    fun findByDeletedAtIsNull(): List<SandboxTemplate>
    fun findByDeletedAtIsNullAndSandboxStatus(sandboxStatus: String): List<SandboxTemplate>
    fun findByDeletedAtIsNullAndSandboxFolderNameContaining(sandboxFolderName: String): List<SandboxTemplate>
    fun findByDeletedAtIsNullAndIsActive(isActive: Boolean): List<SandboxTemplate>
    fun findByDeletedAtIsNullAndIsActiveAndSandboxStatus(isActive: Boolean, sandboxStatus: String): List<SandboxTemplate>
    fun findByIdAndDeletedAtIsNull(id: Long): SandboxTemplate?
}