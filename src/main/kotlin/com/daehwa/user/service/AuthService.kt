package com.daehwa.user.service

import com.daehwa.user.config.TokenProperty
import com.daehwa.user.config.TokenProvider
import com.daehwa.user.dto.SignInRequest
import com.daehwa.user.dto.SignInResponse
import com.daehwa.user.dto.SignUpRequest
import com.daehwa.user.dto.TokenResponse
import com.daehwa.user.dto.UserJwtToken
import com.daehwa.user.model.User
import com.daehwa.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
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
    fun signUp(request: SignUpRequest): User {
        val (email, password, name, nickname) = request
        validateEmail(email)

        return userRepository.save(
            User(
                email = email,
                password = passwordEncoder.encode(password),
                name = name,
                nickname = nickname,
                enabled = true,
                deleted = false,
            )
        )
    }

    private fun validateEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("이미 존재하는 email 입니다")
        }
    }

    @Transactional
    fun signIn(request: SignInRequest): SignInResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw NotFoundException()

        validateUser(user, request.password)

        val refreshToken = createRefreshJwt(user)
        val jwtToken = createAccessJwt(user, refreshToken)

        return SignInResponse(
            email = user.email,
            TokenResponse(
                accessToken = jwtToken.accessToken,
                refreshToken = refreshToken,
            )
        )
    }

    private fun createAccessJwt(user: User, refreshToken: String): UserJwtToken {
        val userJwtToken = tokenProvider.createAccessToken(user, refreshToken)
        user.updateNonce(userJwtToken.nonce)

        return userJwtToken
    }

    private fun createRefreshJwt(user: User): String {
        val refreshToken = tokenProvider.createRefreshToken()
        user.updateRefreshToken(
            refreshTokenExpiredAt = LocalDateTime.now().plusHours(tokenProperty.refreshTokenRenewHour)
        )
        return refreshToken
    }

    private fun validateUser(user: User?, password: String) {
        if (user == null) {
            throw IllegalArgumentException("회원이 존재하지 않습니다")
        }

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다")
        }
    }
}
