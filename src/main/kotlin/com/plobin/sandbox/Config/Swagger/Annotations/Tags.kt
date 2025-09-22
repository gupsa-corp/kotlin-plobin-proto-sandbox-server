package com.plobin.sandbox.Config.Swagger.Annotations

import io.swagger.v3.oas.annotations.tags.Tag

object Tags {

    @get:Tag(name = "Sandbox Template", description = "샌드박스 템플릿 관리 API")
    val SandboxTemplate = Unit

    @get:Tag(name = "Sandbox Template Version", description = "샌드박스 템플릿 버전 관리 API")
    val SandboxTemplateVersion = Unit
}