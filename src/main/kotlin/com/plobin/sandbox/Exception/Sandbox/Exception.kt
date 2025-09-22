package com.plobin.sandbox.Exception.Sandbox

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