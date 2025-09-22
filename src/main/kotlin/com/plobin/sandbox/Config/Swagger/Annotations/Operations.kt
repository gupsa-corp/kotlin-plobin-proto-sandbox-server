package com.plobin.sandbox.Config.Swagger.Annotations

import io.swagger.v3.oas.annotations.Operation

object Operations {

    // SandboxTemplate Operations
    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 목록 조회",
        description = "삭제되지 않은 모든 ${ResourceNames.SANDBOX_TEMPLATE}의 목록을 조회합니다."
    )
    val SandboxTemplateList = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 상세 조회",
        description = "특정 ${ResourceNames.SANDBOX_TEMPLATE}의 상세 정보를 조회합니다."
    )
    val SandboxTemplateGet = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 생성",
        description = "새로운 ${ResourceNames.SANDBOX_TEMPLATE}을 생성합니다."
    )
    val SandboxTemplateCreate = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 수정",
        description = "기존 ${ResourceNames.SANDBOX_TEMPLATE}의 정보를 수정합니다."
    )
    val SandboxTemplateUpdate = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE} 삭제",
        description = "특정 ${ResourceNames.SANDBOX_TEMPLATE}을 삭제합니다."
    )
    val SandboxTemplateDelete = Unit

    // SandboxTemplateVersion Operations
    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 목록 조회",
        description = "삭제되지 않은 모든 ${ResourceNames.SANDBOX_TEMPLATE_VERSION}의 목록을 조회합니다."
    )
    val SandboxTemplateVersionList = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 상세 조회",
        description = "특정 ${ResourceNames.SANDBOX_TEMPLATE_VERSION}의 상세 정보를 조회합니다."
    )
    val SandboxTemplateVersionGet = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 생성",
        description = "새로운 ${ResourceNames.SANDBOX_TEMPLATE_VERSION}을 생성합니다."
    )
    val SandboxTemplateVersionCreate = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 수정",
        description = "기존 ${ResourceNames.SANDBOX_TEMPLATE_VERSION}의 정보를 수정합니다."
    )
    val SandboxTemplateVersionUpdate = Unit

    @get:Operation(
        summary = "${ResourceNames.SANDBOX_TEMPLATE_VERSION} 삭제",
        description = "특정 ${ResourceNames.SANDBOX_TEMPLATE_VERSION}을 삭제합니다."
    )
    val SandboxTemplateVersionDelete = Unit
}