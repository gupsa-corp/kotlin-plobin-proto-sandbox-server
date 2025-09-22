package com.plobin.sandbox.Exception.SandboxVersion

class Exception(message: String) : RuntimeException(message) {
    companion object {
        fun notFound(id: Long): Exception {
            return Exception("SandboxVersion with id $id not found")
        }

        fun notFoundBySandboxId(sandboxId: Long): Exception {
            return Exception("SandboxVersion with sandboxId $sandboxId not found")
        }
    }
}