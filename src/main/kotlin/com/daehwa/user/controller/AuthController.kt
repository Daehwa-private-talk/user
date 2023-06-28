package com.daehwa.user.controller

import com.daehwa.user.model.SignUpRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/api/auth")
class AuthController {

    @PostMapping("sign-up")
    fun signUp(@RequestBody @Valid request: SignUpRequest): String {
        return "test"
    }
}
