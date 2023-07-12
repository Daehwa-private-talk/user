package com.daehwa.user.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

object CookieUtils {
    private const val DAEHWA_DOMAIN = ".daehwa.com"

    fun addCookie(
        response: HttpServletResponse,
        key: String,
        value: String,
        path: String,
        minuteMaxAge: Int,
        httpOnly: Boolean = false,
        secured: Boolean = false
    ) {
        val cookie = Cookie(key, value).apply {
            this.domain = DAEHWA_DOMAIN
            this.path = path
            this.isHttpOnly = httpOnly
            this.maxAge = minuteMaxAge * 60
            this.secure = secured
        }

        response.addCookie(cookie)
    }
}
