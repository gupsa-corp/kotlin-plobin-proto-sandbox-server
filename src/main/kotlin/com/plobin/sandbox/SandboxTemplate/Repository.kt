package com.plobin.sandbox.SandboxTemplate

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Repository : JpaRepository<Entity, Long> {
    fun findByDeletedAtIsNull(): List<Entity>
    fun findByDeletedAtIsNullAndSandboxStatus(sandboxStatus: String): List<Entity>
    fun findByDeletedAtIsNullAndSandboxFolderNameContaining(sandboxFolderName: String): List<Entity>
    fun findByDeletedAtIsNullAndIsActive(isActive: Boolean): List<Entity>
    fun findByDeletedAtIsNullAndIsActiveAndSandboxStatus(isActive: Boolean, sandboxStatus: String): List<Entity>
    fun findByIdAndDeletedAtIsNull(id: Long): Entity?
}