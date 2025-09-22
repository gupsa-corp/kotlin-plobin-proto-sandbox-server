package com.plobin.sandbox.Repository.SandboxTemplateVersion

import jakarta.persistence.*
import java.time.LocalDateTime

@jakarta.persistence.Entity(name = "SandboxTemplateVersion")
@Table(name = "sandbox_template_versions")
data class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "sandbox_template_id", nullable = false)
    val sandboxTemplateId: Long = 0,

    @Column(name = "version_name", nullable = false, length = 100)
    val versionName: String = "",

    @Column(name = "version_number", nullable = false, length = 50)
    val versionNumber: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sandbox_template_id", insertable = false, updatable = false)
    val sandboxTemplate: com.plobin.sandbox.Repository.SandboxTemplate.Entity? = null
)