package com.daehwa.user.dto

data class SignInResponse(
    val email: String,
    val token: TokenResponse
)
