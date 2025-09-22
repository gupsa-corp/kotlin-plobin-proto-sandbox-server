package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Delete.Controller
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.*

class SandboxTemplateVersionDeleteControllerTest {

    private lateinit var repository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        repository = mockk()
        controller = Controller(repository)
    }

    @Test
    fun `기존_버전_삭제를_테스트한다`() {
        // Given
        val versionId = 100L
        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Version to delete",
            createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.deleteById(versionId) } returns Unit

        // When
        val responseEntity = controller.deleteVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }
        verify(exactly = 1) { repository.deleteById(versionId) }

        assertNotNull(responseEntity)
        assertTrue(responseEntity.statusCode.is2xxSuccessful)
        val response = responseEntity.body!!
        assertEquals(versionId, response.id)
        assertEquals("Version successfully deleted", response.message)
        assertEquals("v1.0.0", response.versionName)
        assertEquals("1.0.0", response.versionNumber)
    }

    @Test
    fun `존재하지_않는_버전_삭제시_null_반환을_테스트한다`() {
        // Given
        val nonExistentId = 999L

        every { repository.findById(nonExistentId) } returns Optional.empty()

        // When
        val responseEntity = controller.deleteVersion(nonExistentId)

        // Then
        verify(exactly = 1) { repository.findById(nonExistentId) }
        verify(exactly = 0) { repository.deleteById(any()) }
        assertTrue(responseEntity.statusCode.is4xxClientError)
    }

    @Test
    fun `버전_조회_후_삭제_호출_순서를_테스트한다`() {
        // Given
        val versionId = 101L
        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 2L,
            versionName = "v2.0.0",
            versionNumber = "2.0.0",
            description = null,
            createdAt = LocalDateTime.now().minusDays(5),
            updatedAt = LocalDateTime.now().minusDays(2)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.deleteById(versionId) } returns Unit

        // When
        val responseEntity = controller.deleteVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }
        verify(exactly = 1) { repository.deleteById(versionId) }

        assertNotNull(responseEntity)
        assertTrue(responseEntity.statusCode.is2xxSuccessful)
        val response = responseEntity.body!!
        assertEquals(versionId, response.id)
        assertEquals("v2.0.0", response.versionName)
        assertEquals("2.0.0", response.versionNumber)
    }

    @Test
    fun `null_설명을_가진_엔티티_삭제를_테스트한다`() {
        // Given
        val versionId = 102L
        val existingEntity = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 3L,
            versionName = "v3.0.0-alpha",
            versionNumber = "3.0.0-alpha",
            description = null,
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now().minusDays(1)
        )

        every { repository.findById(versionId) } returns Optional.of(existingEntity)
        every { repository.deleteById(versionId) } returns Unit

        // When
        val responseEntity = controller.deleteVersion(versionId)

        // Then
        verify(exactly = 1) { repository.findById(versionId) }
        verify(exactly = 1) { repository.deleteById(versionId) }

        assertNotNull(responseEntity)
        assertTrue(responseEntity.statusCode.is2xxSuccessful)
        val response = responseEntity.body!!
        assertEquals(versionId, response.id)
        assertEquals("v3.0.0-alpha", response.versionName)
        assertEquals("3.0.0-alpha", response.versionNumber)
        assertEquals("Version successfully deleted", response.message)
    }
}