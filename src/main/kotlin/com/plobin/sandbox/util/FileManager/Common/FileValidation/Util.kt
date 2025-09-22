package com.plobin.sandbox.util.FileManager.Common.FileValidation

import com.plobin.sandbox.Config.FileUpload.Config as FileUploadConfig
import org.springframework.stereotype.Component

@Component("fileValidationUtil")
class Util(private val fileUploadConfig: FileUploadConfig) {

    /**
     * 파일명 보안 검증
     */
    operator fun invoke(fileName: String): Boolean {
        // null, 빈 문자열 체크
        if (fileName.isBlank()) return false

        // 경로 탐색 공격 방지
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return false
        }

        // 허용된 확장자 체크
        val extension = fileName.substringAfterLast(".", "").lowercase()
        return extension.isEmpty() || fileUploadConfig.getAllowedExtensions().contains(extension)
    }
}