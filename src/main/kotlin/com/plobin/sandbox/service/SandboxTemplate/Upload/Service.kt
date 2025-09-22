package com.plobin.sandbox.service.SandboxTemplate.Upload

import com.plobin.sandbox.Repository.SandboxTemplate.Entity as SandboxTemplate
import com.plobin.sandbox.Repository.SandboxTemplate.Repository as SandboxTemplateRepository
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersion
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplate.Upload.Request
import com.plobin.sandbox.controller.SandboxTemplate.Upload.Response
import com.plobin.sandbox.util.FileManager.CreateVersionId.Util as CreateVersionIdUtil
import com.plobin.sandbox.util.FileManager.CreateDirectoryStructure.Util as CreateDirectoryStructureUtil
import com.plobin.sandbox.util.FileManager.SaveUploadedFiles.Util as SaveUploadedFilesUtil
import com.plobin.sandbox.util.FileManager.SetAsRelease.Util as SetAsReleaseUtil
import com.plobin.sandbox.util.FileManager.GetFullFolderPath.Util as GetFullFolderPathUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class Service(
    private val sandboxTemplateRepository: SandboxTemplateRepository,
    private val sandboxTemplateVersionRepository: SandboxTemplateVersionRepository,
    private val createVersionIdUtil: CreateVersionIdUtil,
    private val createDirectoryStructureUtil: CreateDirectoryStructureUtil,
    private val saveUploadedFilesUtil: SaveUploadedFilesUtil,
    private val setAsReleaseUtil: SetAsReleaseUtil,
    private val getFullFolderPathUtil: GetFullFolderPathUtil
) {

    operator fun invoke(request: Request): Response {
        try {
            // 1. 입력값 검증
            validateRequest(request)

            // 2. 템플릿 존재 확인 또는 생성
            val template = findOrCreateTemplate(request.templateName, request.description)

            // 3. 버전 ID 생성
            val versionNumber = if (request.isRelease) "Release" else createVersionIdUtil()

            // 4. 디렉토리 구조 생성
            val targetPath = createDirectoryStructureUtil(request.templateName, versionNumber)

            // 5. 파일 저장
            val uploadedFiles = saveUploadedFilesUtil(request.files, targetPath)

            // 6. 버전 정보 DB에 저장
            val version = createTemplateVersion(template, versionNumber, request.description)

            // 7. Release 처리 (필요한 경우)
            if (request.isRelease && versionNumber != "Release") {
                setAsReleaseUtil(request.templateName, versionNumber)
            }

            // 8. 전체 경로 생성
            val fullFolderPath = getFullFolderPathUtil(request.templateName, versionNumber)

            // 9. 응답 생성
            return Response.fromEntities(
                template = template,
                version = version,
                uploadedFiles = uploadedFiles,
                fullFolderPath = fullFolderPath,
                isRelease = request.isRelease
            )

        } catch (exception: Exception) {
            // 에러 발생 시 롤백 및 정리
            handleUploadError(exception, request.templateName)
            throw exception
        }
    }

    /**
     * 요청 데이터 검증
     */
    private fun validateRequest(request: Request) {
        // 템플릿명 검증
        if (request.templateName.isBlank()) {
            throw IllegalArgumentException("Template name cannot be blank")
        }

        // 파일 존재 검증
        if (request.files.isEmpty()) {
            throw IllegalArgumentException("At least one file must be provided")
        }

        // 파일 크기 및 타입 검증
        request.files.forEach { file ->
            if (file.isEmpty) {
                throw IllegalArgumentException("Empty files are not allowed")
            }
            if (file.originalFilename.isNullOrBlank()) {
                throw IllegalArgumentException("File name cannot be blank")
            }
        }
    }

    /**
     * 템플릿 찾기 또는 생성
     */
    private fun findOrCreateTemplate(templateName: String, description: String?): SandboxTemplate {
        // 기존 템플릿 찾기
        val existingTemplate = sandboxTemplateRepository.findBySandboxFolderNameAndIsActiveTrue(templateName)

        return existingTemplate ?: run {
            // 새 템플릿 생성
            val newTemplate = SandboxTemplate(
                sandboxFolderName = templateName,
                sandboxFolderPath = "sandbox-templates/$templateName",
                sandboxFullFolderPath = getFullFolderPathUtil(templateName, ""),
                sandboxStatus = "ACTIVE",
                description = description,
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            sandboxTemplateRepository.save(newTemplate)
        }
    }

    /**
     * 템플릿 버전 생성
     */
    private fun createTemplateVersion(
        template: SandboxTemplate,
        versionNumber: String,
        description: String?
    ): SandboxTemplateVersion {

        // 동일한 버전이 이미 있는지 확인
        val existingVersion = sandboxTemplateVersionRepository
            .findBySandboxTemplateIdAndVersionNumber(template.id, versionNumber)

        if (existingVersion != null) {
            // 기존 버전이 있으면 업데이트
            val updatedVersion = existingVersion.copy(
                description = description ?: existingVersion.description,
                updatedAt = LocalDateTime.now()
            )
            return sandboxTemplateVersionRepository.save(updatedVersion)
        }

        // 새 버전 생성
        val newVersion = SandboxTemplateVersion(
            sandboxTemplateId = template.id,
            versionName = if (versionNumber == "Release") "Release" else "Version $versionNumber",
            versionNumber = versionNumber,
            description = description,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return sandboxTemplateVersionRepository.save(newVersion)
    }

    /**
     * 업로드 에러 처리
     */
    private fun handleUploadError(exception: Exception, templateName: String) {
        // 로깅
        println("Upload error for template '$templateName': ${exception.message}")

        // 필요시 생성된 파일들 정리 (선택적)
        // 트랜잭션 롤백으로 DB는 자동으로 정리됨
    }
}