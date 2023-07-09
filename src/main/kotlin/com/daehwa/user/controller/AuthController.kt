package com.daehwa.user.controller

import com.daehwa.user.dto.SignInRequest
import com.daehwa.user.dto.SignInResponse
import com.daehwa.user.dto.SignUpRequest
import com.daehwa.user.dto.base_response.SuccessResponse
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

    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid request: SignInRequest): SuccessResponse<SignInResponse> =
        SuccessResponse.of(authService.signIn(request))
}
