package com.daehwa.user.controller

import com.daehwa.user.dto.SignUpRequest
import com.daehwa.user.model.base_response.SuccessResponse
import com.daehwa.user.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid request: SignUpRequest): SuccessResponse<Unit> {
        authService.signUp(request)
        return SuccessResponse.DEFAULT
    }
}
