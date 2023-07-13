package com.daehwa.user.common.exception


class DaehwaException(
    val errorCode: ErrorCode,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
