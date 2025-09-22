package com.plobin.sandbox.controller.Sandbox.Get

import com.plobin.sandbox.Repository.Sandbox.Entity
import com.plobin.sandbox.Repository.Sandbox.Repository as SandboxRepository
import com.plobin.sandbox.Exception.Sandbox.Exception as SandboxException
import org.springframework.web.bind.annotation.RestController

@RestController("sandboxGetController")
class Controller(private val sandboxRepository: SandboxRepository) {

    fun getSandbox(id: Long): Response {
        val sandbox: Entity = sandboxRepository.findByIdAndDeletedAtIsNull(id)
            ?: throw SandboxException.notFound(id)
        return Response.fromEntity(sandbox)
    }
}