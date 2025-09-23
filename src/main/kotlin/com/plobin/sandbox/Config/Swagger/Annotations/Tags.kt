package com.plobin.sandbox.Config.Swagger.Annotations

import io.swagger.v3.oas.annotations.tags.Tag

object Tags {

    @get:Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
    val SandboxTemplate = Unit

    @get:Tag(name = "Sandbox Template Version", description = "샌드박스 템플릿 버전 관리 API")
    val SandboxTemplateVersion = Unit

    @get:Tag(name = "Sandbox", description = "샌드박스 관리 API")
    val Sandbox = Unit

    @get:Tag(name = "Sandbox Version", description = "샌드박스 버전 관리 API")
    val SandboxVersion = Unit

    @get:Tag(name = "SSH", description = "SSH 터미널 연결 관리 API")
    val SSH = Unit
}