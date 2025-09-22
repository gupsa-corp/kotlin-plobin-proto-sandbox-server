package com.plobin.sandbox.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.controller.Sandbox.Create.Request
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Transactional
class SandboxCreateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var sandboxRepository: SandboxRepository

    @Test
    fun test_샌드박스_생성이_성공적으로_처리된다() {
        // Given
        val request = Request(
            uuid = "test-uuid-12345",
            name = "테스트 샌드박스",
            description = "테스트용 샌드박스입니다",
            folderPath = "Assets/sandbox-lists/test-uuid-12345",
            templateId = 1L,
            status = "active",
            createdBy = 1L,
            isActive = true
        )

        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/sandboxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value("test-uuid-12345"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("테스트 샌드박스"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("active"))
    }

    @Test
    fun test_필수_필드_누락_시_유효성_검사가_실패한다() {
        // Given
        val request = mapOf(
            "name" to "테스트 샌드박스",
            // uuid 누락
            "folderPath" to "Assets/sandbox-lists/test",
            "templateId" to 1L
        )

        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/sandboxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}