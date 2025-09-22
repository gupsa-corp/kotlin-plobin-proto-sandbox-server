package com.plobin.sandbox.util.FileManager.SaveUploadedFiles

import com.plobin.sandbox.util.FileManager.Common.FileValidation.Util as FileValidationUtil
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Component
class Util(private val fileValidationUtil: FileValidationUtil) {

    /**
     * 업로드된 파일들을 지정된 경로에 저장
     */
    operator fun invoke(files: List<MultipartFile>, targetPath: Path): List<String> {
        val savedFileNames = mutableListOf<String>()

        files.forEach { file ->
            if (!file.isEmpty) {
                val originalFileName = file.originalFilename ?: "unknown"

                // 파일명 보안 검증
                if (fileValidationUtil(originalFileName)) {
                    when {
                        originalFileName.lowercase().endsWith(".zip") -> {
                            // ZIP 파일인 경우 압축 해제
                            extractZipFile(file, targetPath)
                            savedFileNames.add("${originalFileName} (extracted)")
                        }
                        else -> {
                            // 일반 파일 저장
                            val targetFile = targetPath.resolve(originalFileName)
                            file.transferTo(targetFile)
                            savedFileNames.add(originalFileName)
                        }
                    }
                }
            }
        }

        return savedFileNames
    }

    /**
     * ZIP 파일 압축 해제 (내부 메서드)
     */
    private fun extractZipFile(zipFile: MultipartFile, targetPath: Path) {
        ZipInputStream(zipFile.inputStream).use { zipInputStream ->
            var entry: ZipEntry? = zipInputStream.nextEntry

            while (entry != null) {
                val entryName = entry.name

                // 경로 탐색 공격 방지
                if (fileValidationUtil(entryName) && !entryName.contains("..")) {
                    val entryPath = targetPath.resolve(entryName)

                    if (entry.isDirectory) {
                        Files.createDirectories(entryPath)
                    } else {
                        // 상위 디렉토리가 없으면 생성
                        Files.createDirectories(entryPath.parent)

                        // 파일 생성
                        FileOutputStream(entryPath.toFile()).use { outputStream ->
                            zipInputStream.copyTo(outputStream)
                        }
                    }
                }

                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }
    }
}