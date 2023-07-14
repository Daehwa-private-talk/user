package com.daehwa.user.service

import com.daehwa.user.common.exception.DaehwaException
import com.daehwa.user.common.exception.ErrorCode
import com.daehwa.user.config.TokenProperty
import com.daehwa.user.config.TokenProvider
import com.daehwa.user.dto.SignInRequest
import com.daehwa.user.dto.SignInResponse
import com.daehwa.user.dto.SignUpRequest
import com.daehwa.user.dto.TokenResponse
import com.daehwa.user.model.DaehwaUser
import com.daehwa.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tokenProperty: TokenProperty,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun signUp(request: SignUpRequest): DaehwaUser {
        val (email, password, name, nickname) = request
        validateEmail(email)

        return userRepository.save(
            DaehwaUser(
                email = email,
                password = passwordEncoder.encode(password),
                name = name,
                nickname = nickname,
            )
        )
    }

    private fun validateEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw DaehwaException(ErrorCode.DUPLICATED, "이미 존재하는 email 입니다")
        }
    }

    @Transactional
    fun signIn(request: SignInRequest): SignInResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw DaehwaException(ErrorCode.NOT_FOUND)

        validateUser(user, request.password)

        val refreshToken = createRefreshJwt(user)
        val accessToken = createAccessJwt(user, refreshToken)

        return SignInResponse(
            email = user.email,
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        )
    }

    private fun createAccessJwt(user: DaehwaUser, refreshToken: String): String {
        val token = tokenProvider.createAccessToken(user, refreshToken)
        user.updateSignInAt(LocalDateTime.now())

        return token
    }

    private fun createRefreshJwt(user: DaehwaUser): String {
        val refreshToken = tokenProvider.createRefreshToken()
        user.updateRefreshToken(
            refreshToken = refreshToken,
            refreshTokenExpiredAt = LocalDateTime.now().plusHours(tokenProperty.refreshTokenRenewHour)
        )
        return refreshToken
    }

    private fun validateUser(user: DaehwaUser?, password: String) {
        if (user == null) {
            throw DaehwaException(ErrorCode.NOT_FOUND, "회원이 존재하지 않습니다")
        }

        if (!passwordEncoder.matches(password, user.password)) {
            throw DaehwaException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다")
        }
    }

    @Transactional
    fun refresh(refreshToken: String): TokenResponse {
        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw DaehwaException(ErrorCode.NOT_FOUND, "refresh 대상 회원이 존재하지 않습니다.")

        val newRefreshToken = createRefreshJwt(user)

        return TokenResponse(
            refreshToken = newRefreshToken,
            accessToken = createAccessJwt(user, newRefreshToken)
        )
    }
}
