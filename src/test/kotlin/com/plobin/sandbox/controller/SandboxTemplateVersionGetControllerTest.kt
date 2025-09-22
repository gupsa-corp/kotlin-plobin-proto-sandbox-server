package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Get.Controller
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.*

class SandboxTemplateVersionGetControllerTest {

    private lateinit var repository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        repository = mockk()
        controller = Controller(repository)
    }

    @Test
    fun `버전_조회_성공을_테스트한다`() {
        // Given
        val versionId = 100L
        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "First version",
            createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)

        // When
        val response = controller.getVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }

        assertNotNull(response)
        assertEquals(versionId, response!!.id)
        assertEquals(1L, response.sandboxTemplateId)
        assertEquals("v1.0.0", response.versionName)
        assertEquals("1.0.0", response.versionNumber)
        assertEquals("First version", response.description)
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), response.createdAt)
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), response.updatedAt)
    }

    @Test
    fun `존재하지_않는_버전_조회시_null_반환을_테스트한다`() {
        // Given
        val nonExistentId = 999L

        every { repository.findById(nonExistentId) } returns Optional.empty()

        // When
        val response = controller.getVersion(nonExistentId)

        // Then
        verify(exactly = 1) { repository.findById(nonExistentId) }
        assertNull(response)
    }

    @Test
    fun `null_설명을_가진_엔티티_조회를_테스트한다`() {
        // Given
        val versionId = 101L
        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 2L,
            versionName = "v2.0.0-beta",
            versionNumber = "2.0.0-beta",
            description = null,
            createdAt = LocalDateTime.of(2024, 1, 15, 14, 30),
            updatedAt = LocalDateTime.of(2024, 1, 16, 9, 45)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)

        // When
        val response = controller.getVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }

        assertNotNull(response)
        assertEquals(versionId, response!!.id)
        assertEquals(2L, response.sandboxTemplateId)
        assertEquals("v2.0.0-beta", response.versionName)
        assertEquals("2.0.0-beta", response.versionNumber)
        assertNull(response.description)
        assertEquals(LocalDateTime.of(2024, 1, 15, 14, 30), response.createdAt)
        assertEquals(LocalDateTime.of(2024, 1, 16, 9, 45), response.updatedAt)
    }

    @Test
    fun `모든_엔티티_필드_매핑을_테스트한다`() {
        // Given
        val versionId = 102L
        val createdTime = LocalDateTime.of(2023, 12, 1, 8, 0)
        val updatedTime = LocalDateTime.of(2024, 1, 20, 16, 30)

        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 5L,
            versionName = "v3.2.1",
            versionNumber = "3.2.1",
            description = "Bug fix release with critical patches",
            createdAt = createdTime,
            updatedAt = updatedTime
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)

        // When
        val response = controller.getVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }

        assertNotNull(response)
        with(response!!) {
            assertEquals(existingEntity.id, id)
            assertEquals(existingEntity.sandboxTemplateId, sandboxTemplateId)
            assertEquals(existingEntity.versionName, versionName)
            assertEquals(existingEntity.versionNumber, versionNumber)
            assertEquals(existingEntity.description, description)
            assertEquals(existingEntity.createdAt, createdAt)
            assertEquals(existingEntity.updatedAt, updatedAt)
        }
    }
}