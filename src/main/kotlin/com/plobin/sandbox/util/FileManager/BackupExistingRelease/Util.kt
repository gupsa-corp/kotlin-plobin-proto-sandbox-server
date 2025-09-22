package com.plobin.sandbox.util.FileManager.BackupExistingRelease

import com.plobin.sandbox.Config.FileUpload.Config as FileUploadConfig
import com.plobin.sandbox.util.FileManager.Common.DirectoryOperations.Util as DirectoryOperationsUtil
import com.plobin.sandbox.util.FileManager.CreateVersionId.Util as CreateVersionIdUtil
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component("backupExistingReleaseUtil")
class Util(
    private val fileUploadConfig: FileUploadConfig,
    private val directoryOperationsUtil: DirectoryOperationsUtil,
    private val createVersionIdUtil: CreateVersionIdUtil
) {

    /**
     * 기존 Release 폴더를 백업
     */
    operator fun invoke(templateName: String) {
        val basePath = Paths.get(fileUploadConfig.getBaseUploadPath())
        val templatePath = basePath.resolve(templateName)
        val releasePath = templatePath.resolve("Release")

        if (Files.exists(releasePath)) {
            val backupName = "Release_backup_${createVersionIdUtil()}"
            val backupPath = templatePath.resolve(backupName)

            directoryOperationsUtil.copyDirectory(releasePath, backupPath)
            directoryOperationsUtil.deleteDirectory(releasePath)
        }
    }
}