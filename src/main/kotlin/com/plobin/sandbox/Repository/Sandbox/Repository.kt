package com.plobin.sandbox.Repository.Sandbox

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository("sandboxRepository")
interface Repository : JpaRepository<Entity, Long> {
    fun findByDeletedAtIsNull(): List<Entity>
    fun findByDeletedAtIsNullAndStatus(status: String): List<Entity>
    fun findByDeletedAtIsNullAndNameContaining(name: String): List<Entity>
    fun findByDeletedAtIsNullAndIsActive(isActive: Boolean): List<Entity>
    fun findByDeletedAtIsNullAndIsActiveAndStatus(isActive: Boolean, status: String): List<Entity>

    @Query("SELECT s FROM Sandbox s LEFT JOIN FETCH s.versions WHERE s.id = :id AND s.deletedAt IS NULL")
    fun findByIdAndDeletedAtIsNull(@Param("id") id: Long): Entity?

    fun findByUuidAndDeletedAtIsNull(uuid: String): Entity?
    fun findByTemplateIdAndDeletedAtIsNull(templateId: Long): List<Entity>
    fun findByUuidAndIsActive(uuid: String, isActive: Boolean): Entity?
}