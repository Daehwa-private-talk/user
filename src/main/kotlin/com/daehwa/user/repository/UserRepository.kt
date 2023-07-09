package com.daehwa.user.repository

import com.daehwa.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
    fun existsByEmail(email: String): Boolean
}
