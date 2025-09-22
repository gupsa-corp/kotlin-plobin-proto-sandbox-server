package com.plobin.sandbox.util.FileManager.Common.DirectoryOperations

import org.springframework.stereotype.Component
import java.nio.file.*
import java.util.*

@Component("directoryOperationsUtil")
class Util {

    /**
     * 디렉토리 복사
     */
    fun copyDirectory(source: Path, target: Path) {
        Files.walk(source).forEach { sourcePath ->
            val targetPath = target.resolve(source.relativize(sourcePath))

            if (Files.isDirectory(sourcePath)) {
                Files.createDirectories(targetPath)
            } else {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    /**
     * 디렉토리 삭제
     */
    fun deleteDirectory(path: Path) {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach { Files.delete(it) }
        }
    }
}