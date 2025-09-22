package com.plobin.sandbox.Repository.Sandbox

import jakarta.persistence.*
import java.time.LocalDateTime

@jakarta.persistence.Entity(name = "Sandbox")
@Table(name = "sandboxes")
data class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(name = "uuid", nullable = false, unique = true, length = 255)
    val uuid: String = "",

    @Column(name = "name", nullable = false, length = 255)
    val name: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "folder_path", nullable = false, length = 1000)
    val folderPath: String = "",

    @Column(name = "template_id", nullable = false)
    val templateId: Long = 0,

    @Column(name = "status", nullable = false, length = 50)
    val status: String = "active",

    @Column(name = "created_by", nullable = true)
    val createdBy: Long? = null,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    val sandboxTemplate: com.plobin.sandbox.Repository.SandboxTemplate.Entity? = null,

    @OneToMany(mappedBy = "sandbox", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val versions: List<com.plobin.sandbox.Repository.SandboxVersion.Entity> = emptyList()
)