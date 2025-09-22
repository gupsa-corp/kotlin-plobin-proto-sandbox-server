package com.plobin.sandbox.util.FileManager.CreateVersionId

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component("createVersionIdUtil")
class Util {

    /**
     * 버전 ID 생성 (v + YYMMddHHmm 형식)
     * 예: v2509221647
     */
    operator fun invoke(): String {
        val formatter = DateTimeFormatter.ofPattern("yyMMddHHmm")
        return "v${LocalDateTime.now().format(formatter)}"
    }
}