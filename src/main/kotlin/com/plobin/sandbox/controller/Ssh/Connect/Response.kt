package com.plobin.sandbox.controller.Ssh.Connect

data class Response(
    val success: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val sessionId: String,
        val websocketUrl: String,
        val connectionId: String
    )
}