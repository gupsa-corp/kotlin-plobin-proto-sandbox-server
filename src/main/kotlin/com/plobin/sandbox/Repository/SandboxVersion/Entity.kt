package com.plobin.sandbox.Repository.SandboxVersion

import jakarta.persistence.*
import java.time.LocalDateTime

@jakarta.persistence.Entity(name = "SandboxVersion")
@Table(name = "sandbox_versions")
data class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "sandbox_id", nullable = false)
    val sandboxId: Long = 0,

    @Column(name = "template_version_id", nullable = false)
    val templateVersionId: Long = 0,

    @Column(name = "version_name", nullable = false, length = 100)
    val versionName: String = "",

    @Column(name = "version_path", nullable = false, length = 1000)
    val versionPath: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "is_current", nullable = false)
    val isCurrent: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sandbox_id", insertable = false, updatable = false)
    val sandbox: com.plobin.sandbox.Repository.Sandbox.Entity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_version_id", insertable = false, updatable = false)
    val templateVersion: com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity? = null
)