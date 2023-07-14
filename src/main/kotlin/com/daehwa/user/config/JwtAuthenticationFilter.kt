package com.daehwa.user.config

import com.daehwa.user.common.exception.DaehwaException
import com.daehwa.user.common.exception.ErrorCode
import com.daehwa.user.model.DaehwaUser
import com.daehwa.user.repository.UserRepository
import com.daehwa.user.utils.CookieUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    companion object {
        private const val COOKIE_KEY = "daehwa.access_token"
        private const val ACCESS_TOKEN_EXPIRE_HOUR = 2L
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = tokenProvider.resolveAccessToken(request)

        if (validateToken(token)) {
            val refreshToken = tokenProvider.getRefreshToken(token!!)

            val user = userRepository.findByRefreshToken(refreshToken) ?: kotlin.run {
                expireAccessToken(request, response)
                throw DaehwaException(ErrorCode.DUPLICATED_LOGIN, "중복 로그인 되었습니다.")
            }

            validateSignInAt(user.signInAt!!)

            setAuthentication(user)
        }

        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String?) = tokenProvider.isValidateToken(token)
    private fun validateSignInAt(signInAt: LocalDateTime) {
        if (signInAt.plusHours(ACCESS_TOKEN_EXPIRE_HOUR).isBefore(LocalDateTime.now())) {
            throw DaehwaException(ErrorCode.EXPIRED, "만료된 access token 입니다.")
        }
    }

    private fun expireAccessToken(request: HttpServletRequest, response: HttpServletResponse) {
        request.cookies?.firstOrNull { it.name == COOKIE_KEY }?.let {
            CookieUtils.addCookie(
                response = response,
                key = COOKIE_KEY,
                value = "",
                path = "/",
                minuteMaxAge = 0,
                httpOnly = true,
                secured = true
            )
        }
    }

    private fun setAuthentication(user: DaehwaUser) {
        val authentication = tokenProvider.getAuthentication(user)
        SecurityContextHolder.getContext().authentication = authentication
    }
}
