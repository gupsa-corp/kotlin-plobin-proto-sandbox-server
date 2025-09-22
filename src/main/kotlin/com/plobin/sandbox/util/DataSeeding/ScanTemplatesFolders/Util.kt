package com.plobin.sandbox.util.DataSeeding.ScanTemplatesFolders

import com.plobin.sandbox.util.DataSeeding.ScanTemplateVersions.Util as ScanTemplateVersionsUtil
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Component("scanTemplatesFoldersUtil")
class Util(
    private val scanTemplateVersionsUtil: ScanTemplateVersionsUtil
) {

    /**
     * sandbox-templates 폴더 스캔
     */
    operator fun invoke(): List<TemplateFolder> {
        val templatesPath = Paths.get("Assets/sandbox-templates")
        val result = mutableListOf<TemplateFolder>()

        if (!Files.exists(templatesPath)) {
            println("Templates folder not found: $templatesPath")
            return result
        }

        Files.newDirectoryStream(templatesPath).use { stream ->
            for (templateDir in stream) {
                if (Files.isDirectory(templateDir)) {
                    val templateName = templateDir.fileName.toString()
                    val versions = scanTemplateVersionsUtil(templateDir)

                    result.add(
                        TemplateFolder(
                            name = templateName,
                            path = "sandbox-templates/$templateName",
                            fullPath = templateDir.toAbsolutePath().toString(),
                            versions = versions
                        )
                    )
                }
            }
        }

        return result
    }

}

/**
 * 스캔된 템플릿 폴더 정보
 */
data class TemplateFolder(
    val name: String,
    val path: String,
    val fullPath: String,
    val versions: List<VersionFolder>
)

/**
 * 스캔된 템플릿 버전 폴더 정보
 */
data class VersionFolder(
    val name: String,
    val path: String,
    val isRelease: Boolean
)