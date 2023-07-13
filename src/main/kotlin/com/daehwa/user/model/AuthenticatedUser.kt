package com.daehwa.user.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AuthenticatedUser(
    name: String,
    password: String,
    authorities: List<GrantedAuthority> = emptyList(),
    val id: Int,
    val email: String,
    val nickname: String,
) : User(name, password, authorities)
