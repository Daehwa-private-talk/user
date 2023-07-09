package com.daehwa.user.service

import com.daehwa.user.dto.SignUpRequest
import com.daehwa.user.model.User
import com.daehwa.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
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
            )
        )
    }

    private fun validateEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("이미 존재하는 email 입니다")
        }
    }
}
