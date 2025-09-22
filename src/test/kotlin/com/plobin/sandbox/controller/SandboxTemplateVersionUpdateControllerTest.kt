package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Update.Controller
import com.plobin.sandbox.controller.SandboxTemplateVersion.Update.Request
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.*

class SandboxTemplateVersionUpdateControllerTest {

    private lateinit var repository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        repository = mockk()
        controller = Controller(repository)
    }

    @Test
    fun `기존_버전_업데이트를_테스트한다`() {
        // Given
        val versionId = 100L
        val request = Request(
            versionName = "v1.1.0",
            versionNumber = "1.1.0",
            description = "Updated version"
        )

        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Initial version",
            createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
        )

        val updatedEntity = existingEntity.copy(
            versionName = request.versionName,
            versionNumber = request.versionNumber,
            description = request.description,
            updatedAt = LocalDateTime.of(2024, 1, 2, 10, 0)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.save(any()) } returns updatedEntity

        // When
        val response = controller.updateVersion(versionId, request)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }
        verify(exactly = 1) {
            repository.save(match { entity ->
                entity.id == versionId &&
                entity.versionName == request.versionName &&
                entity.versionNumber == request.versionNumber &&
                entity.description == request.description &&
                entity.sandboxTemplateId == existingEntity.sandboxTemplateId &&
                entity.createdAt == existingEntity.createdAt
            })
        }

        assertNotNull(response)
        assertEquals(versionId, response!!.id)
        assertEquals("v1.1.0", response.versionName)
        assertEquals("1.1.0", response.versionNumber)
        assertEquals("Updated version", response.description)
        assertEquals(existingEntity.createdAt, response.createdAt)
        assertTrue(response.updatedAt.isAfter(existingEntity.updatedAt))
    }

    @Test
    fun `존재하지_않는_버전_업데이트시_null_반환을_테스트한다`() {
        // Given
        val nonExistentId = 999L
        val request = Request(
            versionName = "v1.1.0",
            versionNumber = "1.1.0"
        )

        every { repository.findById(nonExistentId) } returns Optional.empty()

        // When
        val response = controller.updateVersion(nonExistentId, request)

        // Then
        verify(exactly = 1) { repository.findById(nonExistentId) }
        verify(exactly = 0) { repository.save(any()) }
        assertNull(response)
    }

    @Test
    fun `null_설명으로_버전_업데이트를_테스트한다`() {
        // Given
        val versionId = 101L
        val request = Request(
            versionName = "v2.0.0",
            versionNumber = "2.0.0",
            description = null
        )

        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 2L,
            versionName = "v1.9.0",
            versionNumber = "1.9.0",
            description = "Previous description",
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now().minusDays(1)
        )

        val updatedEntity = existingEntity.copy(
            versionName = request.versionName,
            versionNumber = request.versionNumber,
            description = null,
            updatedAt = LocalDateTime.now()
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.save(any()) } returns updatedEntity

        // When
        val response = controller.updateVersion(versionId, request)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }
        verify(exactly = 1) { repository.save(any()) }

        assertNotNull(response)
        assertEquals(versionId, response!!.id)
        assertEquals("v2.0.0", response.versionName)
        assertEquals("2.0.0", response.versionNumber)
        assertNull(response.description)
    }

    @Test
    fun `원본_생성일시와_템플릿_ID_보존을_테스트한다`() {
        // Given
        val versionId = 102L
        val originalCreatedAt = LocalDateTime.of(2023, 12, 1, 9, 0)
        val originalSandboxTemplateId = 5L

        val request = Request(
            versionName = "v3.0.0",
            versionNumber = "3.0.0",
            description = "Major update"
        )

        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = originalSandboxTemplateId,
            versionName = "v2.9.0",
            versionNumber = "2.9.0",
            description = "Previous version",
            createdAt = originalCreatedAt,
            updatedAt = LocalDateTime.of(2023, 12, 15, 10, 0)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.save(any()) } answers { firstArg() }

        // When
        val response = controller.updateVersion(versionId, request)

        // Then
        verify(exactly = 1) {
            repository.save(match { entity ->
                entity.createdAt == originalCreatedAt &&
                entity.sandboxTemplateId == originalSandboxTemplateId &&
                entity.updatedAt.isAfter(existingEntity.updatedAt)
            })
        }

        assertEquals(originalCreatedAt, response!!.createdAt)
        assertEquals(originalSandboxTemplateId, response.sandboxTemplateId)
    }
}