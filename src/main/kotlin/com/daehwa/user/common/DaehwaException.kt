package com.daehwa.user.common


class DaehwaException(
    val errorCode: ErrorCode,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
