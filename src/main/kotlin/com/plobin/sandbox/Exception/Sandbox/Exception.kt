package com.plobin.sandbox.Exception.Sandbox

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class Exception(message: String) : RuntimeException(message) {
    companion object {
        fun notFound(id: Long): Exception {
            return Exception("Sandbox with id $id not found or deleted")
        }

        fun notFoundByUuid(uuid: String): Exception {
            return Exception("Sandbox with uuid $uuid not found or deleted")
        }
    }
}