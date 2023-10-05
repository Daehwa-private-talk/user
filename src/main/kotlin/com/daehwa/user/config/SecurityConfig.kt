package com.daehwa.user.config

import com.daehwa.user.utils.HttpServletUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.saml2.Saml2LoginConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider.ResponseToken
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
//    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter,
) {
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun configure(http: HttpSecurity): SecurityFilterChain {
        val authenticationProvider = OpenSaml4AuthenticationProvider()
        authenticationProvider.setResponseAuthenticationConverter(groupsConverter())
        http.authorizeHttpRequests { authorize ->
            authorize
                .anyRequest().authenticated()
        }
            .saml2Login { saml2: Saml2LoginConfigurer<HttpSecurity?> ->
                saml2
                    .authenticationManager(ProviderManager(authenticationProvider))
            }
            .saml2Logout(withDefaults())
        return http.build()
    }

    private fun groupsConverter(): Converter<ResponseToken, Saml2Authentication> {
        val delegate: Converter<ResponseToken, Saml2Authentication> =
            OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter()
        return Converter<ResponseToken, Saml2Authentication> { responseToken ->
            val authentication: Saml2Authentication = delegate.convert(responseToken)!!
            val principal = authentication.principal as Saml2AuthenticatedPrincipal
            val groups = principal.getAttribute<String>("groups")
            val authorities: MutableSet<GrantedAuthority> = HashSet()
            groups?.stream()?.map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }?.forEach { e: SimpleGrantedAuthority -> authorities.add(e) }
                ?: authorities.addAll(authentication.authorities)
            Saml2Authentication(principal, authentication.saml2Response, authorities)
        }
    }

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http.defaultConfigure()
//            .authorizeHttpRequests {
//                it
//                    .requestMatchers(
//                        *SecurityRequestUriUtils.getGlobalAllowedUris().toTypedArray()
//                    ).permitAll()
//                    .anyRequest().authenticated()
//            }
//
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter::class.java)
//
//        return http.build()
//    }

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
