package com.daehwa.user.utils

object SecurityRequestUriUtils {
    private val GLOBAL_ALLOWED_URIS = listOf(
        "/swagger-ui/**",
        "/api-docs/swagger-config",
        "/api-docs",
        "/v1/api/auth/sign-up",
        "/v1/api/auth/sign-in",
    )

    fun getGlobalAllowedUris(): List<String> {
        return GLOBAL_ALLOWED_URIS
    }
}
