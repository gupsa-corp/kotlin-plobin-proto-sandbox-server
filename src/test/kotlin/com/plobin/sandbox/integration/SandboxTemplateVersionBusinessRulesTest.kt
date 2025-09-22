package com.plobin.sandbox.integration

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Controller as CreateController
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Request as CreateRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class SandboxTemplateVersionBusinessRulesTest @Autowired constructor(
    private val entityManager: TestEntityManager,
    private val repository: SandboxTemplateVersionRepository
) {

    private lateinit var createController: CreateController
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        createController = CreateController(repository)
        val validatorFactory = LocalValidatorFactoryBean()
        validatorFactory.afterPropertiesSet()
        validator = validatorFactory.validator
    }

    @Test
    fun `비즈니스_규칙_템플릿_ID는_양수여야_함을_테스트한다`() {
        // Test with zero
        val requestWithZero = CreateRequest(
            sandboxTemplateId = 0L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0"
        )

        val violationsZero: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithZero)
        assertFalse(violationsZero.isEmpty())
        assertTrue(violationsZero.any { it.message.contains("positive") })

        // Test with negative
        val requestWithNegative = CreateRequest(
            sandboxTemplateId = -1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0"
        )

        val violationsNegative: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithNegative)
        assertFalse(violationsNegative.isEmpty())
        assertTrue(violationsNegative.any { it.message.contains("positive") })

        // Test with positive (should pass)
        val requestWithPositive = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0"
        )

        val violationsPositive: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithPositive)
        assertTrue(violationsPositive.isEmpty())
    }

    @Test
    fun `비즈니스_규칙_버전명은_공백일_수_없음을_테스트한다`() {
        // Test with empty string
        val requestWithEmpty = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "",
            versionNumber = "1.0.0"
        )

        val violationsEmpty: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithEmpty)
        assertFalse(violationsEmpty.isEmpty())
        assertTrue(violationsEmpty.any { it.message.contains("required") })

        // Test with whitespace only
        val requestWithWhitespace = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "   ",
            versionNumber = "1.0.0"
        )

        val violationsWhitespace: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithWhitespace)
        assertFalse(violationsWhitespace.isEmpty())
        assertTrue(violationsWhitespace.any { it.message.contains("required") })
    }

    @Test
    fun `비즈니스_규칙_버전명_길이_제한을_테스트한다`() {
        // Test with exactly 100 characters (should pass)
        val exactlyMaxLength = "v" + "1".repeat(99)
        val requestExactMax = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = exactlyMaxLength,
            versionNumber = "1.0.0"
        )

        val violationsExactMax: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestExactMax)
        assertTrue(violationsExactMax.isEmpty())

        // Test with 101 characters (should fail)
        val tooLong = "v" + "1".repeat(100)
        val requestTooLong = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = tooLong,
            versionNumber = "1.0.0"
        )

        val violationsTooLong: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestTooLong)
        assertFalse(violationsTooLong.isEmpty())
        assertTrue(violationsTooLong.any { it.message.contains("100 characters") })
    }

    @Test
    fun `비즈니스_규칙_버전번호는_공백일_수_없음을_테스트한다`() {
        val requestWithEmptyVersionNumber = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = ""
        )

        val violations: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithEmptyVersionNumber)
        assertFalse(violations.isEmpty())
        assertTrue(violations.any { it.message.contains("required") })
    }

    @Test
    fun `비즈니스_규칙_버전번호_길이_제한을_테스트한다`() {
        // Test with exactly 50 characters (should pass)
        val exactlyMaxLength = "1".repeat(50)
        val requestExactMax = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = exactlyMaxLength
        )

        val violationsExactMax: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestExactMax)
        assertTrue(violationsExactMax.isEmpty())

        // Test with 51 characters (should fail)
        val tooLong = "1".repeat(51)
        val requestTooLong = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = tooLong
        )

        val violationsTooLong: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestTooLong)
        assertFalse(violationsTooLong.isEmpty())
        assertTrue(violationsTooLong.any { it.message.contains("50 characters") })
    }

    @Test
    fun `비즈니스_규칙_설명은_선택사항임을_테스트한다`() {
        // Test with null description (should pass)
        val requestWithNullDesc = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = null
        )

        val violations: Set<ConstraintViolation<CreateRequest>> = validator.validate(requestWithNullDesc)
        assertTrue(violations.isEmpty())

        // Should be able to create successfully
        val response = createController.createVersion(requestWithNullDesc)
        assertNotNull(response)
        assertNull(response.description)
    }

    @Test
    fun `비즈니스_규칙_타임스탬프_자동_관리를_테스트한다`() {
        val request = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Timestamp test"
        )

        val beforeCreation = System.currentTimeMillis()
        val response = createController.createVersion(request)
        val afterCreation = System.currentTimeMillis()

        assertNotNull(response)

        // Check that createdAt and updatedAt are set and within reasonable bounds
        val createdAtMillis = response.createdAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val updatedAtMillis = response.updatedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

        assertTrue(createdAtMillis >= beforeCreation - 1000) // Allow 1 second tolerance
        assertTrue(createdAtMillis <= afterCreation + 1000)
        assertTrue(updatedAtMillis >= beforeCreation - 1000)
        assertTrue(updatedAtMillis <= afterCreation + 1000)

        // Initially, createdAt and updatedAt should be the same
        assertEquals(response.createdAt, response.updatedAt)
    }

    @Test
    fun `비즈니스_규칙_템플릿_내_버전_고유성을_테스트한다`() {
        // Create first version
        val firstVersion = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 1L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "First version"
            )
        )

        assertNotNull(firstVersion)

        // Create second version with different version number (should succeed)
        val secondVersion = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 1L,
                versionName = "v1.1.0",
                versionNumber = "1.1.0",
                description = "Second version"
            )
        )

        assertNotNull(secondVersion)

        // Verify both versions exist
        val template1Versions = repository.findBySandboxTemplateId(1L)
        assertEquals(2, template1Versions.size)

        // Verify we can find each version uniquely
        val found100 = repository.findBySandboxTemplateIdAndVersionNumber(1L, "1.0.0")
        val found110 = repository.findBySandboxTemplateIdAndVersionNumber(1L, "1.1.0")

        assertNotNull(found100)
        assertNotNull(found110)
        assertNotEquals(found100!!.id, found110!!.id)
    }

    @Test
    fun `비즈니스_규칙_다른_템플릿_간_동일_버전번호_허용을_테스트한다`() {
        // Create version 1.0.0 for template 1
        val template1Version = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 1L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "Template 1 version"
            )
        )

        // Create version 1.0.0 for template 2
        val template2Version = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 2L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "Template 2 version"
            )
        )

        assertNotNull(template1Version)
        assertNotNull(template2Version)
        assertNotEquals(template1Version.id, template2Version.id)

        // Verify both can be found uniquely
        val found1 = repository.findBySandboxTemplateIdAndVersionNumber(1L, "1.0.0")
        val found2 = repository.findBySandboxTemplateIdAndVersionNumber(2L, "1.0.0")

        assertNotNull(found1)
        assertNotNull(found2)
        assertEquals(template1Version.id, found1!!.id)
        assertEquals(template2Version.id, found2!!.id)
    }
}