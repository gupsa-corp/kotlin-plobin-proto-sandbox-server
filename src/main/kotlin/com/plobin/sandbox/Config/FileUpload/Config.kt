package com.plobin.sandbox.config.FileUpload

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.unit.DataSize
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.multipart.support.StandardServletMultipartResolver
import jakarta.servlet.MultipartConfigElement

@Configuration
class Config {

    @Value("\${app.upload.base-path:./Assets/sandbox-templates}")
    lateinit var basePath: String

    @Value("\${app.upload.max-file-size:50MB}")
    lateinit var maxFileSize: String

    @Value("\${app.upload.max-request-size:100MB}")
    lateinit var maxRequestSize: String

    @Bean
    fun multipartResolver(): MultipartResolver {
        return StandardServletMultipartResolver()
    }

    @Bean
    fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()

        // 최대 파일 크기 설정
        factory.setMaxFileSize(DataSize.parse(maxFileSize))

        // 최대 요청 크기 설정
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize))

        return factory.createMultipartConfig()
    }

    /**
     * 허용되는 파일 확장자들
     */
    fun getAllowedExtensions(): Set<String> {
        return setOf(
            "html", "css", "js", "json", "xml", "txt", "md",
            "png", "jpg", "jpeg", "gif", "svg", "ico",
            "zip", "tar", "gz"
        )
    }

    /**
     * 기본 업로드 경로 반환
     */
    fun getBaseUploadPath(): String = basePath
}