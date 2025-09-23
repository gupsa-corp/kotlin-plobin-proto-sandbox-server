package com.plobin.sandbox.controller.SshTerminal

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller("sshTerminalController")
@RequestMapping("/ssh-terminal")
class Controller {

    @GetMapping
    fun sshTerminal(): String {
        return "redirect:/700-page-ssh/000-index.html"
    }
}