package com.plobin.sandbox.util.FileManager.CreateDirectoryStructure

import com.plobin.sandbox.Config.FileUpload.Config as FileUploadConfig
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Component
class Util(private val fileUploadConfig: FileUploadConfig) {

    /**
     * 디렉토리 구조 생성
     * 예: Assets/sandbox-templates/gupsa/v2509221647/
     */
    operator fun invoke(templateName: String, versionId: String): Path {
        val basePath = Paths.get(fileUploadConfig.getBaseUploadPath())
        val targetPath = basePath.resolve(templateName).resolve(versionId)

        Files.createDirectories(targetPath)
        return targetPath
    }
}