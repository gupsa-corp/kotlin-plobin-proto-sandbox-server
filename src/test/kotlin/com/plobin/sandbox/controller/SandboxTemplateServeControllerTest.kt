package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplateEntity
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplate.Serve.Controller
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.core.io.InputStreamResource
import java.time.LocalDateTime
import java.util.*

class SandboxTemplateServeControllerTest {

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
    fun `서빙_정보_조회를_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 10L

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
        val response = controller.getServeInfo(templateId, versionId)

        // Then
        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }

        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)

        with(response.body!!) {
            assertEquals(templateId, this.templateId)
            assertEquals("test-template", templateName)
            assertEquals(versionId, this.versionId)
            assertEquals("v1.0.0", versionName)
            assertEquals("1.0.0", versionNumber)
            assertEquals("/api/sandbox-templates/$templateId/serve?versionId=$versionId", downloadUrl)
            assertEquals("application/zip", contentType)
        }
    }

    @Test
    fun `버전_ID_없이_서빙_정보_조회를_테스트한다`() {
        // Given
        val templateId = 1L

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

        val latestVersion = SandboxTemplateVersionEntity(
            id = 20L,
            sandboxTemplateId = templateId,
            versionName = "v2.0.0",
            versionNumber = "2.0.0",
            description = "Latest version",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns template
        every { versionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId) } returns latestVersion

        // When
        val response = controller.getServeInfo(templateId, null)

        // Then
        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId) }

        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)

        with(response.body!!) {
            assertEquals(templateId, this.templateId)
            assertEquals("test-template", templateName)
            assertEquals(20L, this.versionId)
            assertEquals("v2.0.0", versionName)
            assertEquals("2.0.0", versionNumber)
            assertEquals("/api/sandbox-templates/$templateId/serve", downloadUrl)
        }
    }

    @Test
    fun `존재하지_않는_템플릿_서빙_정보_조회시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 999L

        every { templateRepository.findByIdAndDeletedAtIsNull(templateId) } returns null

        // When & Then
        assertThrows<RuntimeException> {
            controller.getServeInfo(templateId, null)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 0) { versionRepository.findById(any()) }
        verify(exactly = 0) { versionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(any()) }
    }

    @Test
    fun `존재하지_않는_버전_서빙_정보_조회시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 999L

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
            controller.getServeInfo(templateId, versionId)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }
    }

    @Test
    fun `다른_템플릿의_버전_서빙_정보_조회시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 1L
        val versionId = 10L

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
            controller.getServeInfo(templateId, versionId)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findById(versionId) }
    }

    @Test
    fun `버전이_없는_템플릿_서빙_정보_조회시_예외_발생을_테스트한다`() {
        // Given
        val templateId = 1L

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
        every { versionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId) } returns null

        // When & Then
        assertThrows<RuntimeException> {
            controller.getServeInfo(templateId, null)
        }

        verify(exactly = 1) { templateRepository.findByIdAndDeletedAtIsNull(templateId) }
        verify(exactly = 1) { versionRepository.findTopBySandboxTemplateIdOrderByCreatedAtDesc(templateId) }
    }
}