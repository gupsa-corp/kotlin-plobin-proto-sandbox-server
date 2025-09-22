package com.plobin.sandbox.util.DataSeeding.ScanSandboxFolders

import com.plobin.sandbox.util.DataSeeding.ScanSandboxVersions.Util as ScanSandboxVersionsUtil
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Component("scanSandboxFoldersUtil")
class Util(
    private val scanSandboxVersionsUtil: ScanSandboxVersionsUtil
) {

    /**
     * sandbox-lists 폴더 스캔
     */
    operator fun invoke(): List<SandboxFolder> {
        val sandboxListsPath = Paths.get("Assets/sandbox-lists")
        val result = mutableListOf<SandboxFolder>()

        if (!Files.exists(sandboxListsPath)) {
            println("Sandbox lists folder not found: $sandboxListsPath")
            return result
        }

        Files.newDirectoryStream(sandboxListsPath).use { stream ->
            for (sandboxDir in stream) {
                if (Files.isDirectory(sandboxDir)) {
                    val uuid = sandboxDir.fileName.toString()
                    val versions = scanSandboxVersionsUtil(sandboxDir)

                    result.add(
                        SandboxFolder(
                            uuid = uuid,
                            path = "Assets/sandbox-lists/$uuid",
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
 * 스캔된 샌드박스 폴더 정보
 */
data class SandboxFolder(
    val uuid: String,
    val path: String,
    val versions: List<SandboxVersionFolder>
)

/**
 * 스캔된 샌드박스 버전 폴더 정보
 */
data class SandboxVersionFolder(
    val name: String,
    val path: String,
    val isCurrent: Boolean
)