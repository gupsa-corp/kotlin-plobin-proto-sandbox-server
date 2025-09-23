package com.plobin.sandbox.model.Ssh

import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.Session
import java.time.LocalDateTime

data class Connection(
    val connectionId: String,
    val sessionId: String,
    val host: String,
    val port: Int,
    val username: String,
    val sshSession: Session?,
    val channel: ChannelShell?,
    val createdAt: LocalDateTime,
    var lastActivity: LocalDateTime,
    var isActive: Boolean
)