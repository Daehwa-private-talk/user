package com.daehwa.user.dto.base_response

import org.springframework.http.HttpStatus

enum class ResponseCode(val code: Int, val httpStatus: HttpStatus) {
    OK(2000, HttpStatus.OK),
    DUPLICATED(2001, HttpStatus.CONFLICT),
    UNAUTHORIZED(4001, HttpStatus.UNAUTHORIZED),
    FORBIDDEN(4003, HttpStatus.FORBIDDEN),
    INVALID_PARAMETER(4100, HttpStatus.BAD_REQUEST),
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST),
}
