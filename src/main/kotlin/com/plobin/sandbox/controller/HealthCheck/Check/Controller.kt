package com.plobin.sandbox.controller.HealthCheck.Check

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class Controller {

    @GetMapping("/health-check")
    fun check(): ResponseEntity<Response> {
        val response = Response(
            success = true,
            message = "서비스가 정상적으로 작동중입니다"
        )
        return ResponseEntity.ok(response)
    }
}