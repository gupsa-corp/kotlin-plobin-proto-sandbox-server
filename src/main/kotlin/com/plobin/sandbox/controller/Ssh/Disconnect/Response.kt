package com.plobin.sandbox.controller.Ssh.Disconnect

data class Response(
    val success: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val sessionId: String
    )
}