package com.plobin.sandbox.util.DataSeeding.ScanSandboxVersions

import com.plobin.sandbox.util.DataSeeding.ScanSandboxFolders.SandboxVersionFolder
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

@Component("scanSandboxVersionsUtil")
class Util {

    /**
     * 샌드박스 내 버전 폴더들 스캔
     */
    operator fun invoke(sandboxDir: Path): List<SandboxVersionFolder> {
        val result = mutableListOf<SandboxVersionFolder>()

        if (!Files.exists(sandboxDir)) {
            return result
        }

        Files.newDirectoryStream(sandboxDir).use { stream ->
            for (versionDir in stream) {
                if (Files.isDirectory(versionDir)) {
                    val versionName = versionDir.fileName.toString()

                    result.add(
                        SandboxVersionFolder(
                            name = versionName,
                            path = versionDir.toAbsolutePath().toString(),
                            isCurrent = versionName == "Release"
                        )
                    )
                }
            }
        }

        return result
    }
}