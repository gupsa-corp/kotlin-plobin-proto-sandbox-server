package com.plobin.sandbox.Exception.SandboxTemplate

class Exception(message: String) : RuntimeException(message) {
    companion object {
        fun notFound(id: Long): Exception {
            return Exception("SandboxTemplate with id $id not found or deleted")
        }
    }
}