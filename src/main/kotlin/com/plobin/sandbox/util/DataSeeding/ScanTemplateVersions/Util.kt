package com.plobin.sandbox.util.DataSeeding.ScanTemplateVersions

import com.plobin.sandbox.util.DataSeeding.ScanTemplatesFolders.VersionFolder
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

@Component("scanTemplateVersionsUtil")
class Util {

    /**
     * 템플릿 내 버전 폴더들 스캔
     */
    operator fun invoke(templateDir: Path): List<VersionFolder> {
        val result = mutableListOf<VersionFolder>()

        if (!Files.exists(templateDir)) {
            return result
        }

        Files.newDirectoryStream(templateDir).use { stream ->
            for (versionDir in stream) {
                if (Files.isDirectory(versionDir)) {
                    val versionName = versionDir.fileName.toString()

                    result.add(
                        VersionFolder(
                            name = versionName,
                            path = versionDir.toAbsolutePath().toString(),
                            isRelease = versionName == "Release"
                        )
                    )
                }
            }
        }

        return result
    }
}