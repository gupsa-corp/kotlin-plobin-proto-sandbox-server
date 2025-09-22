package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplateEntity
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplate.Release.Controller
import com.plobin.sandbox.controller.SandboxTemplate.Release.Request
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class SandboxTemplateReleaseControllerTest {

    private lateinit var templateRepository: SandboxTemplateRepository
    private lateinit var versionRepository: SandboxTemplateVersionRepository
    private lateinit var controller: Controller

    @BeforeEach
    fun setup() {
        templateRepository = mockk()
        versionRepository = mockk()
        controller = Controller(templateRepository, versionRepository)
    }

    @Test
    fun `정상적인_릴리즈_설정을_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 10L
        val request = Request(versionId = versionId)

        val template = SandboxTemplateEntity(
            id = templateId,
            sandboxFolderName = "test-template",
            sandboxFolderPath = "/templates/test",
            sandboxFullFolderPath = "/full/path/templates/test",
            sandboxStatus = "ACTIVE",
            description = "Test template",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val version = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = templateId,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Release version",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns template
        every { versionRepository.findById(versionId) } returns Optional.of(version)

        // When
        val response = controller.setRelease(templateId, request)

        // Then
        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }

        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)

        with(response.body!!) {
            assertEquals(templateId, this.templateId)
            assertEquals("test-template", templateName)
            assertEquals(versionId, releasedVersionId)
            assertEquals("v1.0.0", releasedVersionName)
            assertEquals("1.0.0", releasedVersionNumber)
        }
    }

    @Test
    fun `존재하지_않는_템플릿으로_릴리즈_설정시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 999L
        val versionId = 10L
        val request = Request(versionId = versionId)

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns null

        // When & Then
        assertThrows<RuntimeException> {
            controller.setRelease(templateId, request)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 0) { versionRepository.findById(any()) }
    }

    @Test
    fun `존재하지_않는_버전으로_릴리즈_설정시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 999L
        val request = Request(versionId = versionId)

        val template = SandboxTemplateEntity(
            id = templateId,
            sandboxFolderName = "test-template",
            sandboxFolderPath = "/templates/test",
            sandboxFullFolderPath = "/full/path/templates/test",
            sandboxStatus = "ACTIVE",
            description = "Test template",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns template
        every { versionRepository.findById(versionId) } returns Optional.empty()

        // When & Then
        assertThrows<RuntimeException> {
            controller.setRelease(templateId, request)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }
    }

    @Test
    fun `다른_템플릿의_버전으로_릴리즈_설정시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 10L
        val request = Request(versionId = versionId)

        val template = SandboxTemplateEntity(
            id = templateId,
            sandboxFolderName = "test-template",
            sandboxFolderPath = "/templates/test",
            sandboxFullFolderPath = "/full/path/templates/test",
            sandboxStatus = "ACTIVE",
            description = "Test template",
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val version = SandboxTemplateVersionEntity(
            id = versionId,
            sandboxTemplateId = 999L, // 다른 템플릿 ID
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Release version",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns template
        every { versionRepository.findById(versionId) } returns Optional.of(version)

        // When & Then
        assertThrows<RuntimeException> {
            controller.setRelease(templateId, request)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }
    }
}