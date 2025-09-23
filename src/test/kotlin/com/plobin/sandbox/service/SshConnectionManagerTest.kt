package com.plobin.sandbox.service

import com.plobin.sandbox.service.Ssh.ConnectionManager.Service
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SshConnectionManagerTest {

    private lateinit var connectionManagerService: Service

    @BeforeEach
    fun setUp() {
        connectionManagerService = Service()
    }

    @Test
    fun test_SSH_연결_생성이_실패한다_잘못된_호스트() {
        val sessionId = UUID.randomUUID().toString()

        assertThrows<Exception> {
            connectionManagerService.createConnection(
                sessionId = sessionId,
                host = "invalid-host",
                port = 22,
                username = "testuser",
                password = "testpass"
            )
        }
    }

    @Test
    fun test_존재하지_않는_세션_조회시_null_반환() {
        val sessionId = UUID.randomUUID().toString()

        val connection = connectionManagerService.getConnection(sessionId)

        assertNull(connection)
    }

    @Test
    fun test_활성_세션_목록이_비어있다() {
        val activeSessions = connectionManagerService.getAllActiveSessions()

        assertTrue(activeSessions.isEmpty())
    }

    @Test
    fun test_연결_개수가_0이다() {
        val count = connectionManagerService.getConnectionCount()

        assertEquals(0, count)
    }

    @Test
    fun test_존재하지_않는_세션_제거시_예외_발생하지_않음() {
        val sessionId = UUID.randomUUID().toString()

        assertDoesNotThrow {
            connectionManagerService.removeConnection(sessionId)
        }
    }
}