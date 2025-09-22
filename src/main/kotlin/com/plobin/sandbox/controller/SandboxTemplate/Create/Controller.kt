package com.plobin.sandbox.controller.SandboxTemplate.Create

import com.plobin.sandbox.service.SandboxTemplate.Create.Service as CreateService
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val createService: CreateService) {

    operator fun invoke(request: Request): Response {
        return createService(request)
    }
}