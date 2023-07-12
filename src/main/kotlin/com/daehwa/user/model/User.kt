package com.daehwa.user.model

import com.daehwa.user.model.base_entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.Where
import java.time.LocalDateTime

@Entity
@Where(clause = "is_deleted = false")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    var refreshToken: String? = null,
    var refreshTokenExpiredAt: LocalDateTime? = null,
    var signInAt: LocalDateTime? = null,
    @Column(name = "is_enabled", columnDefinition = "TINYINT")
    val enabled: Boolean = true,
    @Column(name = "is_deleted", columnDefinition = "TINYINT")
    val deleted: Boolean = false,
) : BaseEntity() {
    fun updateRefreshToken(
        refreshToken: String,
        refreshTokenExpiredAt: LocalDateTime,
    ) {
        this.refreshToken = refreshToken
        this.refreshTokenExpiredAt = refreshTokenExpiredAt
    }

    fun updateSignInAt(signInAt: LocalDateTime) {
        this.signInAt = signInAt
    }
}
