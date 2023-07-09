package com.daehwa.user.dto

data class UserJwtToken(
    val accessToken: String,
    val nonce: String,
)
