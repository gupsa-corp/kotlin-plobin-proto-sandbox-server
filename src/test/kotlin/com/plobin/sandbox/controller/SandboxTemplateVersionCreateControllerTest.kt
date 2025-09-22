package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Controller
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Request
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class SandboxTemplateVersionCreateControllerTest {

    private lateinit var repository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        repository = mockk()
        controller = Controller(repository)
    }

    @Test
    fun `새로운_버전_생성을_테스트한다`() {
        // Given
        val request = Request(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Initial version"
        )

        val savedEntity = SandboxTemplateVersionEntity(
            id = 100L,
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Initial version",
            createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
        )

        every { repository.save(any()) } returns savedEntity

        // When
        val response = controller.createVersion(request)

        // Then
        verify(exactly = 1) {
            repository.save(match { entity ->
                entity.sandboxTemplateId == request.sandboxTemplateId &&
                entity.versionName == request.versionName &&
                entity.versionNumber == request.versionNumber &&
                entity.description == request.description
            })
        }

        assertEquals(100L, response.id)
        assertEquals(1L, response.sandboxTemplateId)
        assertEquals("v1.0.0", response.versionName)
        assertEquals("1.0.0", response.versionNumber)
        assertEquals("Initial version", response.description)
        assertNotNull(response.createdAt)
        assertNotNull(response.updatedAt)
    }

    @Test
    fun `null_설명을_가진_버전_생성을_테스트한다`() {
        // Given
        val request = Request(
            sandboxTemplateId = 2L,
            versionName = "v2.0.0",
            versionNumber = "2.0.0",
            description = null
        )

        val savedEntity = SandboxTemplateVersionEntity(
            id = 101L,
            sandboxTemplateId = 2L,
            versionName = "v2.0.0",
            versionNumber = "2.0.0",
            description = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { repository.save(any()) } returns savedEntity

        // When
        val response = controller.createVersion(request)

        // Then
        verify(exactly = 1) { repository.save(any()) }
        assertEquals(101L, response.id)
        assertNull(response.description)
    }

    @Test
    fun `생성일시와_수정일시_자동_설정을_테스트한다`() {
        // Given
        val request = Request(
            sandboxTemplateId = 3L,
            versionName = "v3.0.0",
            versionNumber = "3.0.0"
        )

        val beforeTest = LocalDateTime.now().minusSeconds(1)

        every { repository.save(any()) } answers { firstArg() }

        // When
        val response = controller.createVersion(request)

        val afterTest = LocalDateTime.now().plusSeconds(1)

        // Then
        verify(exactly = 1) {
            repository.save(match { entity ->
                entity.createdAt.isAfter(beforeTest) &&
                entity.createdAt.isBefore(afterTest) &&
                entity.updatedAt.isAfter(beforeTest) &&
                entity.updatedAt.isBefore(afterTest)
            })
        }
    }
}