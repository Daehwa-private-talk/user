package com.daehwa.user.config

import com.daehwa.user.config.JasyptConfig.Companion.JASYPT_ENCRYPTOR
import com.daehwa.user.dto.UserJwtToken
import com.daehwa.user.model.User
import com.daehwa.user.utils.UUIDUtils
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.transaction.Transactional
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class TokenProvider(
    private val tokenProperty: TokenProperty,
    @Qualifier(JASYPT_ENCRYPTOR)
    private val jasypt: StringEncryptor,
) {
    private val signingKey = Keys.hmacShaKeyFor(tokenProperty.secretKey.toByteArray())

    fun createRefreshToken(): String = UUIDUtils.generate()

    @Transactional
    fun createAccessToken(user: User, refreshToken: String): UserJwtToken {
        val nonce = jasypt.encrypt(refreshToken + "*" + LocalDateTime.now())
        val claims = getClaims(user.email, nonce)
        val now = Date()

        val jwtToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + tokenProperty.accessTokenExpireTime))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()

        return UserJwtToken(
            accessToken = jwtToken,
            nonce = nonce
        )
    }

    private fun getClaims(email: String, nonce: String) = mapOf("email" to email, "nonce" to nonce)
}
