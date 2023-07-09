package com.daehwa.user.utils

import jakarta.servlet.http.HttpServletRequest

object HttpServletUtils {
    fun getFullURL(request: HttpServletRequest): String {
        val method = request.method
        val requestURL = request.requestURL.toString()
        val queryString = request.queryString

        return if (queryString.isNullOrBlank()) {
            method.plus(" ").plus(requestURL)
        } else {
            method.plus(" ").plus(requestURL).plus("?").plus(queryString)
        }
    }
}
