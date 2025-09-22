package com.plobin.sandbox.controller.SandboxTemplateVersion.Delete

data class Response(
    val id: Long,
    val message: String,
    val versionName: String,
    val versionNumber: String
) {
    companion object {
        fun fromDeletedEntity(entity: com.plobin.sandbox.SandboxTemplateVersion.Entity, message: String = "Version successfully deleted"): Response {
            return Response(
                id = entity.id,
                message = message,
                versionName = entity.versionName,
                versionNumber = entity.versionNumber
            )
        }
    }
}