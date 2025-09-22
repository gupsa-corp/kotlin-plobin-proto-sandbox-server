package com.plobin.sandbox.repository

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplateEntity
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class SandboxTemplateVersionRepositoryTest @Autowired constructor(
    private val entityManager: TestEntityManager,
    private val repository: SandboxTemplateVersionRepository,
    private val sandboxTemplateRepository: SandboxTemplateRepository
) {

    private lateinit var testData: List<SandboxTemplateVersionEntity>
    private lateinit var sandboxTemplate1: SandboxTemplateEntity
    private lateinit var sandboxTemplate2: SandboxTemplateEntity

    @BeforeEach
    fun setup() {
        // Create sandbox templates first
        sandboxTemplate1 = entityManager.persistAndFlush(SandboxTemplateEntity(
            sandboxFolderName = "test-template-1",
            sandboxFolderPath = "/test/template1",
            sandboxFullFolderPath = "/full/test/template1",
            sandboxStatus = "active",
            description = "Test template 1",
            isActive = true,
            createdAt = LocalDateTime.of(2023, 12, 1, 10, 0),
            updatedAt = LocalDateTime.of(2023, 12, 1, 10, 0)
        ))

        sandboxTemplate2 = entityManager.persistAndFlush(SandboxTemplateEntity(
            sandboxFolderName = "test-template-2",
            sandboxFolderPath = "/test/template2",
            sandboxFullFolderPath = "/full/test/template2",
            sandboxStatus = "active",
            description = "Test template 2",
            isActive = true,
            createdAt = LocalDateTime.of(2023, 12, 2, 10, 0),
            updatedAt = LocalDateTime.of(2023, 12, 2, 10, 0)
        ))

        // Create test data
        testData = listOf(
            SandboxTemplateVersionEntity(
                sandboxTemplateId = sandboxTemplate1.id,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "Initial release",
                createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
            ),
            SandboxTemplateVersionEntity(
                sandboxTemplateId = sandboxTemplate1.id,
                versionName = "v1.1.0",
                versionNumber = "1.1.0",
                description = "Feature update",
                createdAt = LocalDateTime.of(2024, 1, 15, 14, 30),
                updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30)
            ),
            SandboxTemplateVersionEntity(
                sandboxTemplateId = sandboxTemplate2.id,
                versionName = "v2.0.0",
                versionNumber = "2.0.0",
                description = null,
                createdAt = LocalDateTime.of(2024, 2, 1, 9, 0),
                updatedAt = LocalDateTime.of(2024, 2, 1, 9, 0)
            ),
            SandboxTemplateVersionEntity(
                sandboxTemplateId = sandboxTemplate1.id,
                versionName = "v1.2.0-beta",
                versionNumber = "1.2.0-beta",
                description = "Beta version",
                createdAt = LocalDateTime.of(2024, 2, 10, 16, 20),
                updatedAt = LocalDateTime.of(2024, 2, 10, 16, 20)
            )
        )

        // Persist test data
        testData.forEach { entity ->
            entityManager.persistAndFlush(entity)
        }
        entityManager.clear()
    }

    @Test
    fun `특정_템플릿의_버전들_조회를_테스트한다`() {
        // When
        val versions = repository.findBySandboxTemplateId(sandboxTemplate1.id)

        // Then
        assertEquals(3, versions.size)
        assertTrue(versions.all { it.sandboxTemplateId == sandboxTemplate1.id })

        val versionNumbers = versions.map { it.versionNumber }.toSet()
        assertTrue(versionNumbers.contains("1.0.0"))
        assertTrue(versionNumbers.contains("1.1.0"))
        assertTrue(versionNumbers.contains("1.2.0-beta"))
    }

    @Test
    fun `존재하지_않는_템플릿에_대해_빈_리스트_반환을_테스트한다`() {
        // When
        val versions = repository.findBySandboxTemplateId(999L)

        // Then
        assertTrue(versions.isEmpty())
    }

    @Test
    fun `버전명_부분_일치_검색을_테스트한다`() {
        // When
        val versions = repository.findByVersionNameContaining("v1")

        // Then
        assertEquals(3, versions.size)
        assertTrue(versions.all { it.versionName.contains("v1") })
    }

    @Test
    fun `일치하지_않는_버전명_검색시_빈_리스트_반환을_테스트한다`() {
        // When
        val versions = repository.findByVersionNameContaining("v9")

        // Then
        assertTrue(versions.isEmpty())
    }

    @Test
    fun `생성일_기준_내림차순_정렬을_테스트한다`() {
        // When
        val versions = repository.findBySandboxTemplateIdOrderByCreatedAtDesc(1L)

        // Then
        assertEquals(3, versions.size)
        assertTrue(versions.all { it.sandboxTemplateId == 1L })

        // Verify descending order by creation date
        for (i in 0 until versions.size - 1) {
            assertTrue(
                versions[i].createdAt.isAfter(versions[i + 1].createdAt) ||
                versions[i].createdAt.isEqual(versions[i + 1].createdAt),
                "Versions should be in descending order by createdAt"
            )
        }

        // First should be the latest (v1.2.0-beta)
        assertEquals("v1.2.0-beta", versions[0].versionName)
    }

    @Test
    fun `템플릿_ID와_버전번호로_특정_버전_조회를_테스트한다`() {
        // When
        val version = repository.findBySandboxTemplateIdAndVersionNumber(1L, "1.0.0")

        // Then
        assertNotNull(version)
        assertEquals(1L, version!!.sandboxTemplateId)
        assertEquals("1.0.0", version.versionNumber)
        assertEquals("v1.0.0", version.versionName)
        assertEquals("Initial release", version.description)
    }

    @Test
    fun `존재하지_않는_조합에_대해_null_반환을_테스트한다`() {
        // When
        val version = repository.findBySandboxTemplateIdAndVersionNumber(1L, "9.9.9")

        // Then
        assertNull(version)
    }

    @Test
    fun `잘못된_템플릿_ID에_대해_null_반환을_테스트한다`() {
        // When
        val version = repository.findBySandboxTemplateIdAndVersionNumber(999L, "1.0.0")

        // Then
        assertNull(version)
    }

    @Test
    fun `null_설명을_가진_엔티티_처리를_테스트한다`() {
        // When
        val version = repository.findBySandboxTemplateIdAndVersionNumber(2L, "2.0.0")

        // Then
        assertNotNull(version)
        assertEquals(2L, version!!.sandboxTemplateId)
        assertEquals("2.0.0", version.versionNumber)
        assertNull(version.description)
    }

    @Test
    fun `새_엔티티_저장과_ID_자동_생성을_테스트한다`() {
        // Given
        val newVersion = SandboxTemplateVersionEntity(
            sandboxTemplateId = 3L,
            versionName = "v3.0.0",
            versionNumber = "3.0.0",
            description = "New template version",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When
        val savedVersion = repository.save(newVersion)
        entityManager.flush()

        // Then
        assertTrue(savedVersion.id > 0)
        assertEquals(3L, savedVersion.sandboxTemplateId)
        assertEquals("v3.0.0", savedVersion.versionName)
        assertEquals("3.0.0", savedVersion.versionNumber)
        assertEquals("New template version", savedVersion.description)

        // Verify it can be found
        val foundVersion = repository.findBySandboxTemplateIdAndVersionNumber(3L, "3.0.0")
        assertNotNull(foundVersion)
        assertEquals(savedVersion.id, foundVersion!!.id)
    }
}