package com.plobin.sandbox.util.FileManager.SetAsRelease

import com.plobin.sandbox.Config.FileUpload.Config as FileUploadConfig
import com.plobin.sandbox.util.FileManager.Common.DirectoryOperations.Util as DirectoryOperationsUtil
import com.plobin.sandbox.util.FileManager.BackupExistingRelease.Util as BackupExistingReleaseUtil
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component("setAsReleaseUtil")
class Util(
    private val fileUploadConfig: FileUploadConfig,
    private val directoryOperationsUtil: DirectoryOperationsUtil,
    private val backupExistingReleaseUtil: BackupExistingReleaseUtil
) {

    /**
     * 특정 버전을 Release로 설정
     * 기존 Release 폴더가 있으면 백업 후 새 버전으로 교체
     */
    operator fun invoke(templateName: String, sourceVersionId: String) {
        val basePath = Paths.get(fileUploadConfig.getBaseUploadPath())
        val templatePath = basePath.resolve(templateName)
        val sourceVersionPath = templatePath.resolve(sourceVersionId)
        val releasePath = templatePath.resolve("Release")

        // 소스 버전 폴더 존재 확인
        if (!Files.exists(sourceVersionPath)) {
            throw IllegalArgumentException("Source version folder does not exist: $sourceVersionId")
        }

        // 기존 Release 폴더 백업
        if (Files.exists(releasePath)) {
            backupExistingReleaseUtil(templateName)
        }

        // 새 버전을 Release로 복사
        directoryOperationsUtil.copyDirectory(sourceVersionPath, releasePath)
    }
}