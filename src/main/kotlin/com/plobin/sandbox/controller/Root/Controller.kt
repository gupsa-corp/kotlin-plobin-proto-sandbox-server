package com.plobin.sandbox.controller.Root

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller("rootController")
class Controller {

    @GetMapping("/")
    fun redirectToSwagger(): String {
        return "redirect:/swagger-ui.html"
    }
}