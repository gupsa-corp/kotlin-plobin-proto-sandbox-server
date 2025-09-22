package com.plobin.sandbox.integration

import com.plobin.sandbox.Repository.SandboxTemplateVersion.Entity as SandboxTemplateVersionEntity
import com.plobin.sandbox.Repository.SandboxTemplateVersion.Repository as SandboxTemplateVersionRepository
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Controller as CreateController
import com.plobin.sandbox.controller.SandboxTemplateVersion.Create.Request as CreateRequest
import com.plobin.sandbox.controller.SandboxTemplateVersion.Update.Controller as UpdateController
import com.plobin.sandbox.controller.SandboxTemplateVersion.Update.Request as UpdateRequest
import com.plobin.sandbox.controller.SandboxTemplateVersion.Delete.Controller as DeleteController
import com.plobin.sandbox.controller.SandboxTemplateVersion.Get.Controller as GetController
import com.plobin.sandbox.controller.SandboxTemplateVersion.List.Controller as ListController
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class SandboxTemplateVersionWorkflowIntegrationTest @Autowired constructor(
    private val entityManager: TestEntityManager,
    private val repository: SandboxTemplateVersionRepository
) {

    private lateinit var createController: CreateController
    private lateinit var updateController: UpdateController
    private lateinit var deleteController: DeleteController
    private lateinit var getController: GetController
    private lateinit var listController: ListController

    @BeforeEach
    fun setup() {
        // Initialize controllers with the same repository
        createController = CreateController(repository)
        updateController = UpdateController(repository)
        deleteController = DeleteController(repository)
        getController = GetController(repository)
        listController = ListController(repository)
    }

    @Test
    fun `전체_CRUD_워크플로우_엔드투엔드_테스트를_한다`() {
        // 1. Initially, repository should be empty
        val initialList = listController.listVersions()
        assertTrue(initialList.isEmpty())

        // 2. Create a new version
        val createRequest = CreateRequest(
            sandboxTemplateId = 1L,
            versionName = "v1.0.0",
            versionNumber = "1.0.0",
            description = "Initial version for integration test"
        )

        val createResponse = createController.createVersion(createRequest)
        assertNotNull(createResponse)
        val createdId = createResponse.id
        assertTrue(createdId > 0)
        assertEquals(1L, createResponse.sandboxTemplateId)
        assertEquals("v1.0.0", createResponse.versionName)
        assertEquals("1.0.0", createResponse.versionNumber)
        assertEquals("Initial version for integration test", createResponse.description)

        // 3. Verify creation by getting the version
        val getResponse = getController.getVersion(createdId)
        assertNotNull(getResponse)
        assertEquals(createdId, getResponse!!.id)
        assertEquals("v1.0.0", getResponse.versionName)
        assertEquals("1.0.0", getResponse.versionNumber)
        assertEquals("Initial version for integration test", getResponse.description)

        // 4. Verify it appears in the list
        val listAfterCreate = listController.listVersions()
        assertEquals(1, listAfterCreate.size)
        assertEquals(createdId, listAfterCreate[0].id)

        // 5. Update the version
        val updateRequest = UpdateRequest(
            versionName = "v1.0.1",
            versionNumber = "1.0.1",
            description = "Updated version with bug fixes"
        )

        val updateResponse = updateController.updateVersion(createdId, updateRequest)
        assertNotNull(updateResponse)
        assertEquals(createdId, updateResponse!!.id)
        assertEquals("v1.0.1", updateResponse.versionName)
        assertEquals("1.0.1", updateResponse.versionNumber)
        assertEquals("Updated version with bug fixes", updateResponse.description)
        assertEquals(createResponse.createdAt, updateResponse.createdAt) // createdAt should not change
        assertTrue(updateResponse.updatedAt.isAfter(createResponse.updatedAt)) // updatedAt should be newer

        // 6. Verify update by getting the version again
        val getAfterUpdate = getController.getVersion(createdId)
        assertNotNull(getAfterUpdate)
        assertEquals("v1.0.1", getAfterUpdate!!.versionName)
        assertEquals("1.0.1", getAfterUpdate.versionNumber)
        assertEquals("Updated version with bug fixes", getAfterUpdate.description)

        // 7. Delete the version
        val deleteResponse = deleteController.deleteVersion(createdId)
        assertNotNull(deleteResponse)
        assertEquals(createdId, deleteResponse!!.id)
        assertEquals("v1.0.1", deleteResponse.versionName)
        assertEquals("1.0.1", deleteResponse.versionNumber)
        assertEquals("Version successfully deleted", deleteResponse.message)

        // 8. Verify deletion - get should return null
        val getAfterDelete = getController.getVersion(createdId)
        assertNull(getAfterDelete)

        // 9. Verify it's removed from the list
        val listAfterDelete = listController.listVersions()
        assertTrue(listAfterDelete.isEmpty())
    }

    @Test
    fun `다른_템플릿에_대한_여러_버전_처리_워크플로우를_테스트한다`() {
        // Create versions for two different templates
        val version1 = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 1L,
                versionName = "v1.0.0",
                versionNumber = "1.0.0",
                description = "Template 1 - Version 1"
            )
        )

        val version2 = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 1L,
                versionName = "v1.1.0",
                versionNumber = "1.1.0",
                description = "Template 1 - Version 2"
            )
        )

        val version3 = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 2L,
                versionName = "v2.0.0",
                versionNumber = "2.0.0",
                description = "Template 2 - Version 1"
            )
        )

        // Verify all versions are in the list
        val allVersions = listController.listVersions()
        assertEquals(3, allVersions.size)

        // Verify repository-specific queries
        val template1Versions = repository.findBySandboxTemplateId(1L)
        assertEquals(2, template1Versions.size)

        val template2Versions = repository.findBySandboxTemplateId(2L)
        assertEquals(1, template2Versions.size)

        // Test version ordering for template 1
        val template1OrderedVersions = repository.findBySandboxTemplateIdOrderByCreatedAtDesc(1L)
        assertEquals(2, template1OrderedVersions.size)
        // The second version should be first (most recent)
        assertEquals("v1.1.0", template1OrderedVersions[0].versionName)
        assertEquals("v1.0.0", template1OrderedVersions[1].versionName)

        // Test finding by version number
        val specificVersion = repository.findBySandboxTemplateIdAndVersionNumber(1L, "1.0.0")
        assertNotNull(specificVersion)
        assertEquals(version1.id, specificVersion!!.id)
    }

    @Test
    fun `엣지_케이스_처리_워크플로우를_테스트한다`() {
        // Test updating non-existent version
        val updateNonExistent = updateController.updateVersion(
            999L,
            UpdateRequest("v9.9.9", "9.9.9", "Non-existent")
        )
        assertNull(updateNonExistent)

        // Test deleting non-existent version
        val deleteNonExistent = deleteController.deleteVersion(999L)
        assertNull(deleteNonExistent)

        // Test getting non-existent version
        val getNonExistent = getController.getVersion(999L)
        assertNull(getNonExistent)

        // Create a version with null description
        val versionWithNullDesc = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 3L,
                versionName = "v3.0.0",
                versionNumber = "3.0.0",
                description = null
            )
        )

        assertNotNull(versionWithNullDesc)
        assertNull(versionWithNullDesc.description)

        // Update to add description
        val updatedWithDesc = updateController.updateVersion(
            versionWithNullDesc.id,
            UpdateRequest("v3.0.1", "3.0.1", "Added description")
        )

        assertNotNull(updatedWithDesc)
        assertEquals("Added description", updatedWithDesc!!.description)

        // Update to remove description
        val updatedWithoutDesc = updateController.updateVersion(
            versionWithNullDesc.id,
            UpdateRequest("v3.0.2", "3.0.2", null)
        )

        assertNotNull(updatedWithoutDesc)
        assertNull(updatedWithoutDesc!!.description)
    }

    @Test
    fun `데이터_무결성_제약_조건_유지_워크플로우를_테스트한다`() {
        // Create a version
        val createdVersion = createController.createVersion(
            CreateRequest(
                sandboxTemplateId = 5L,
                versionName = "v5.0.0",
                versionNumber = "5.0.0",
                description = "Data integrity test"
            )
        )

        val originalCreatedAt = createdVersion.createdAt
        val originalSandboxTemplateId = createdVersion.sandboxTemplateId

        // Wait a moment to ensure different timestamp
        Thread.sleep(10)

        // Update the version
        val updatedVersion = updateController.updateVersion(
            createdVersion.id,
            UpdateRequest("v5.0.1", "5.0.1", "Updated description")
        )

        assertNotNull(updatedVersion)

        // Verify that createdAt and sandboxTemplateId are preserved
        assertEquals(originalCreatedAt, updatedVersion!!.createdAt)
        assertEquals(originalSandboxTemplateId, updatedVersion.sandboxTemplateId)

        // Verify that updatedAt has changed
        assertTrue(updatedVersion.updatedAt.isAfter(originalCreatedAt))

        // Verify the version can still be found by its original template ID
        val foundVersion = repository.findBySandboxTemplateIdAndVersionNumber(
            originalSandboxTemplateId,
            updatedVersion.versionNumber
        )
        assertNotNull(foundVersion)
        assertEquals(updatedVersion.id, foundVersion!!.id)
    }
}