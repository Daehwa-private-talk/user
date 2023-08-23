package com.daehwa.user.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    @Value("\${spring.security.cors.allow-origins}")
    private val allowOrigins: List<String>,
) {
    private val logger = KotlinLogging.logger { }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        logger.debug("allowOrigins: $allowOrigins")
        logger.debug("check cd process")

        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowOrigins
        configuration.allowedHeaders = getAllowHeaders()
        configuration.exposedHeaders = listOf(
            HttpHeaders.CONTENT_DISPOSITION,
            HttpHeaders.CONTENT_TYPE,
            "Authorization",
        )
        configuration.allowedMethods = arrayListOf("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    private fun getAllowHeaders() = arrayListOf(
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers",
        "Origin",
        "Cache-Control",
        "Content-Type",
        "Authorization",
    )
}
