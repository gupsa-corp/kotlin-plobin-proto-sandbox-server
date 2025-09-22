package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.List.Controller
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class SandboxTemplateVersionListControllerTest {

    private lateinit var repository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        repository = mockk()
        controller = Controller(repository)
    }

    @Test
    fun `리포지터리에_데이터가_있을때_전체_버전_목록_반환을_테스트한다`() {
        // Given
        val entities = listOf(
            SandboxTemplateVersionEntity(
                id = 1L,
                sandboxTemplateId = 10L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "Initial release",
                createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
            ),
            SandboxTemplateVersionEntity(
                id = 2L,
                sandboxTemplateId = 10L,
                versionName = "v1.1.0",
                versionNumber = "1.1.0",
                description = "Feature update",
                createdAt = LocalDateTime.of(2024, 1, 15, 14, 30),
                updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30)
            ),
            SandboxTemplateVersionEntity(
                id = 3L,
                sandboxTemplateId = 20L,
                versionName = "v2.0.0",
                versionNumber = "2.0.0",
                description = null,
                createdAt = LocalDateTime.of(2024, 2, 1, 9, 0),
                updatedAt = LocalDateTime.of(2024, 2, 1, 9, 0)
            )
        )

        every { repository.findAll() } returns entities

        // When
        val responses = controller.listVersions()

        // Then
        verify(exactly = 1) { repository.findAll() }

        assertEquals(3, responses.size)

        // Verify first entity mapping
        with(responses[0]) {
            assertEquals(1L, id)
            assertEquals(10L, sandboxTemplateId)
            assertEquals("v1.0.0", versionName)
            assertEquals("1.0.0", versionNumber)
            assertEquals("Initial release", description)
            assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), createdAt)
            assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), updatedAt)
        }

        // Verify second entity mapping
        with(responses[1]) {
            assertEquals(2L, id)
            assertEquals(10L, sandboxTemplateId)
            assertEquals("v1.1.0", versionName)
            assertEquals("1.1.0", versionNumber)
            assertEquals("Feature update", description)
        }

        // Verify third entity mapping with null description
        with(responses[2]) {
            assertEquals(3L, id)
            assertEquals(20L, sandboxTemplateId)
            assertEquals("v2.0.0", versionName)
            assertEquals("2.0.0", versionNumber)
            assertNull(description)
        }
    }

    @Test
    fun `리포지터리가_비어있을때_빈_리스트_반환을_테스트한다`() {
        // Given
        every { repository.findAll() } returns emptyList()

        // When
        val responses = controller.listVersions()

        // Then
        verify(exactly = 1) { repository.findAll() }
        assertTrue(responses.isEmpty())
    }

    @Test
    fun `단일_엔티티_처리를_테스트한다`() {
        // Given
        val singleEntity = SandboxTemplateVersionEntity(
            id = 100L,
            sandboxTemplateId = 5L,
            versionName = "v0.1.0-alpha",
            versionNumber = "0.1.0-alpha",
            description = "Pre-release version",
            createdAt = LocalDateTime.of(2024, 1, 10, 8, 45),
            updatedAt = LocalDateTime.of(2024, 1, 12, 16, 20)
        )

        every { repository.findAll() } returns listOf(singleEntity)

        // When
        val responses = controller.listVersions()

        // Then
        verify(exactly = 1) { repository.findAll() }

        assertEquals(1, responses.size)

        with(responses[0]) {
            assertEquals(100L, id)
            assertEquals(5L, sandboxTemplateId)
            assertEquals("v0.1.0-alpha", versionName)
            assertEquals("0.1.0-alpha", versionNumber)
            assertEquals("Pre-release version", description)
            assertEquals(LocalDateTime.of(2024, 1, 10, 8, 45), createdAt)
            assertEquals(LocalDateTime.of(2024, 1, 12, 16, 20), updatedAt)
        }
    }

    @Test
    fun `리포지터리_순서_보존을_테스트한다`() {
        // Given
        val entities = listOf(
            SandboxTemplateVersionEntity(
                id = 3L,
                sandboxTemplateId = 1L,
                versionName = "v3.0.0",
                versionNumber = "3.0.0",
                description = "Third version",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            SandboxTemplateVersionEntity(
                id = 1L,
                sandboxTemplateId = 1L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "First version",
                createdAt = LocalDateTime.now().minusDays(2),
                updatedAt = LocalDateTime.now().minusDays(2)
            ),
            SandboxTemplateVersionEntity(
                id = 2L,
                sandboxTemplateId = 1L,
                versionName = "v2.0.0",
                versionNumber = "2.0.0",
                description = "Second version",
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now().minusDays(1)
            )
        )

        every { repository.findAll() } returns entities

        // When
        val responses = controller.listVersions()

        // Then
        verify(exactly = 1) { repository.findAll() }

        assertEquals(3, responses.size)
        assertEquals(3L, responses[0].id)
        assertEquals(1L, responses[1].id)
        assertEquals(2L, responses[2].id)
    }
}