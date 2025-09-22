package com.plobin.sandbox.controller

import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Repository.Sandbox.Entity as SandboxEntity
import com.plobin.sandbox.Repository.SandboxVersion.Repository as SandboxVersionRepository
import com.plobin.sandbox.Repository.SandboxVersion.Entity as SandboxVersionEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Transactional
class SandboxDeleteControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var sandboxRepository: SandboxRepository

    @Autowired
    private lateinit var sandboxVersionRepository: SandboxVersionRepository

    @Test
    fun test_샌드박스_삭제가_성공적으로_처리된다() {
        // Given
        val sandbox = SandboxEntity(
            uuid = "test-uuid-12345",
            name = "테스트 샌드박스",
            description = "삭제할 샌드박스",
            folderPath = "Assets/sandbox-lists/test-uuid-12345",
            templateId = 1L,
            status = "active",
            createdBy = 1L,
            isActive = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedSandbox = sandboxRepository.save(sandbox)

        val version = SandboxVersionEntity(
            sandboxId = savedSandbox.id,
            templateVersionId = 1L,
            versionName = "v1.0.0",
            versionPath = "Assets/sandbox-lists/test-uuid-12345/v1.0.0",
            description = "첫 번째 버전",
            isCurrent = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        sandboxVersionRepository.save(version)

        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/sandboxes/${savedSandbox.id}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedSandbox.id))

        // 실제로 삭제되었는지 확인
        val deletedSandbox = sandboxRepository.findById(savedSandbox.id)
        assert(!deletedSandbox.isPresent)

        val deletedVersions = sandboxVersionRepository.findBySandboxId(savedSandbox.id)
        assert(deletedVersions.isEmpty())
    }

    @Test
    fun test_존재하지_않는_샌드박스_삭제_시_404_에러가_발생한다() {
        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/sandboxes/99999")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}