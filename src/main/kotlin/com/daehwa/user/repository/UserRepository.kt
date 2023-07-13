package com.daehwa.user.repository

import com.daehwa.user.model.DaehwaUser
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<DaehwaUser, Int> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): DaehwaUser?
    fun findByRefreshToken(refreshToken: String): DaehwaUser?
}
