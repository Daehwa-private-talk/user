package com.daehwa.user.dto

import jakarta.validation.constraints.Email

data class SignInRequest(
    @field: Email(message = "이메일 형식이 잘못되었습니다.")
    val email: String,
    val password: String,
)
