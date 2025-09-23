package com.plobin.sandbox.controller.Ssh.Connect

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class Request(
    @field:NotBlank(message = "호스트는 필수입니다")
    val host: String,

    @field:Min(value = 1, message = "포트는 1 이상이어야 합니다")
    @field:Max(value = 65535, message = "포트는 65535 이하여야 합니다")
    val port: Int = 22,

    @field:NotBlank(message = "사용자명은 필수입니다")
    val username: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)