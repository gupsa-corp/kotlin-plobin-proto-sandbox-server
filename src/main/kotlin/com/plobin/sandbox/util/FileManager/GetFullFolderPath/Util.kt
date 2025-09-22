package com.plobin.sandbox.util.FileManager.GetFullFolderPath

import com.plobin.sandbox.Config.FileUpload.Config as FileUploadConfig
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component("getFullFolderPathUtil")
class Util(private val fileUploadConfig: FileUploadConfig) {

    /**
     * 템플릿 폴더 전체 경로 생성
     */
    operator fun invoke(templateName: String, versionId: String): String {
        val basePath = Paths.get(fileUploadConfig.getBaseUploadPath())
        return basePath.resolve(templateName).resolve(versionId).toAbsolutePath().toString()
    }
}