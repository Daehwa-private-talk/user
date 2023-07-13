package com.daehwa.user.common.enums

enum class Role {
    USER, ADMIN,;

    fun getRoleName() = "ROLE_${this.name}"
}
