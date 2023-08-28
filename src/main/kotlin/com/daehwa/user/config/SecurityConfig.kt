package com.daehwa.user.config

import com.daehwa.user.utils.HttpServletUtils
import com.daehwa.user.utils.SecurityRequestUriUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter,
) {
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.defaultConfigure()
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        *SecurityRequestUriUtils.getGlobalAllowedUris().toTypedArray()
                    ).permitAll()
                    .anyRequest().authenticated()
            }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter::class.java)

        return http.build()
    }

    private fun HttpSecurity.defaultConfigure(): HttpSecurity {
        return cors { }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .anonymous { }
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                it.accessDeniedHandler(jwtAccessDeniedHandler)
            }
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val logger = KotlinLogging.logger { }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.debug(
            "JwtAuthenticationEntryPoint exception has occurred. URL [${HttpServletUtils.getFullURL(request)}]",
            authException
        )
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
    }
}

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {
    private val logger = KotlinLogging.logger { }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        logger.debug(
            "JwtAccessDeniedHandler exception has occureed. URL [${HttpServletUtils.getFullURL(request)}]",
            accessDeniedException
        )
        response.sendError(HttpServletResponse.SC_FORBIDDEN)
    }
}
