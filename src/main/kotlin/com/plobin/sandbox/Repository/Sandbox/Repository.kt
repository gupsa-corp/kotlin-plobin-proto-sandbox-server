package com.plobin.sandbox.Repository.Sandbox

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("sandboxRepository")
interface Repository : JpaRepository<Entity, Long> {
    fun findByDeletedAtIsNull(): List<Entity>
    fun findByDeletedAtIsNullAndStatus(status: String): List<Entity>
    fun findByDeletedAtIsNullAndNameContaining(name: String): List<Entity>
    fun findByDeletedAtIsNullAndIsActive(isActive: Boolean): List<Entity>
    fun findByDeletedAtIsNullAndIsActiveAndStatus(isActive: Boolean, status: String): List<Entity>
    fun findByIdAndDeletedAtIsNull(id: Long): Entity?
    fun findByUuidAndDeletedAtIsNull(uuid: String): Entity?
    fun findByTemplateIdAndDeletedAtIsNull(templateId: Long): List<Entity>
    fun findByUuidAndIsActiveTrue(uuid: String): Entity?
}