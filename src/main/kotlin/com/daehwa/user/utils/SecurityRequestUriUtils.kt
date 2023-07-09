package com.daehwa.user.utils

object SecurityRequestUriUtils {
    private val GLOBAL_ALLOWED_URIS = listOf(
        "/swagger-ui/**",
        "/api-docs/swagger-config",
        "/api-docs",
        "/v1/api/auth/sign-up"
    )

    fun getGlobalAllowedUris(): List<String> {
        return GLOBAL_ALLOWED_URIS
    }
}
