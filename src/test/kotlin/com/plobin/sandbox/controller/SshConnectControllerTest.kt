package com.plobin.sandbox.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.plobin.sandbox.controller.Ssh.Connect.Request
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class SshConnectControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun test_필수_필드_누락_시_유효성_검사가_실패한다() {
        val request = Request(
            host = "",
            port = 22,
            username = "",
            password = ""
        )

        mockMvc.perform(
            post("/api/ssh/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun test_잘못된_포트_범위로_유효성_검사가_실패한다() {
        val request = Request(
            host = "example.com",
            port = 70000,
            username = "testuser",
            password = "testpass"
        )

        mockMvc.perform(
            post("/api/ssh/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun test_유효한_요청_형식이지만_연결_실패한다() {
        val request = Request(
            host = "invalid-host.example.com",
            port = 22,
            username = "testuser",
            password = "testpass"
        )

        mockMvc.perform(
            post("/api/ssh/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").exists())
    }
}