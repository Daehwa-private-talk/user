package com.daehwa.user.config

import com.daehwa.user.config.JasyptConfig.Companion.JASYPT_ENCRYPTOR
import com.daehwa.user.model.AuthenticatedUser
import com.daehwa.user.model.DaehwaUser
import com.daehwa.user.utils.UUIDUtils
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
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

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    fun createRefreshToken(): String = UUIDUtils.generate()

    @Transactional
    fun createAccessToken(user: DaehwaUser, refreshToken: String): String {
        val nonce = jasypt.encrypt(refreshToken + "*" + LocalDateTime.now())
        val claims = getClaims(user.email, nonce)
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + tokenProperty.accessTokenExpireTime))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getClaims(email: String, nonce: String) = mapOf("email" to email, "nonce" to nonce)

    fun resolveAccessToken(request: HttpServletRequest): String? {
        val accessTokenCookie = request.cookies?.firstOrNull { it.name == "daehwa.access_token" }

        return accessTokenCookie?.value ?: request.getHeader(AUTHORIZATION_HEADER)
    }

    fun isValidateToken(token: String?): Boolean {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body

            isNotExpired(claims)
        } catch (e: Exception) {
            false
        }
    }

    private fun isNotExpired(claims: Claims): Boolean = claims.expiration.after(Date())

    fun getRefreshToken(token: String): String {
        val nonce = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body["nonce"]
            .toString()

        return jasypt.decrypt(nonce).split("*")[0]
    }

    fun getAuthentication(user: DaehwaUser): Authentication {
        val authenticatedUser = AuthenticatedUser(
            name = user.name,
            password = user.password,
            authorities = listOf(SimpleGrantedAuthority(user.role.getRoleName())),
            id = user.id,
            email = user.email,
            nickname = user.nickname,
        )

        return UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.authorities)
    }
}
