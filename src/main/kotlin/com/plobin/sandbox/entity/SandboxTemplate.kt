package com.plobin.sandbox.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sandbox_templates")
data class SandboxTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "sandbox_folder_name", nullable = false, length = 255)
    val sandboxFolderName: String = "",

    @Column(name = "sandbox_folder_path", nullable = false, length = 500)
    val sandboxFolderPath: String = "",

    @Column(name = "sandbox_full_folder_path", nullable = false, length = 1000)
    val sandboxFullFolderPath: String = "",

    @Column(name = "sandbox_status", nullable = false, length = 50)
    val sandboxStatus: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "sandboxTemplate", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val versions: List<SandboxTemplateVersion> = emptyList()
)