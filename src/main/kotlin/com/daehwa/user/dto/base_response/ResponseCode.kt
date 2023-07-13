package com.daehwa.user.dto.base_response

import org.springframework.http.HttpStatus

enum class ResponseCode(val code: Int, val httpStatus: HttpStatus) {
    OK(2000, HttpStatus.OK),
    DUPLICATED(2001, HttpStatus.CONFLICT),
    UNAUTHORIZED(4001, HttpStatus.UNAUTHORIZED),
    FORBIDDEN(4003, HttpStatus.FORBIDDEN),
    NOT_FOUND(4004, HttpStatus.NOT_FOUND),
    INVALID_PARAMETER(4100, HttpStatus.BAD_REQUEST),
    UPLOAD_FAILURE(4101, HttpStatus.BAD_REQUEST),
    ETC(4102, HttpStatus.BAD_REQUEST),
    ILLEGAL_STATE(4103, HttpStatus.BAD_REQUEST),
    UNSUPPORTED(9998, HttpStatus.NOT_ACCEPTABLE),
    UNKNOWN(9999, HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST),
    GONE(4100, HttpStatus.GONE),
    DUPLICATED_LOGIN(4009, HttpStatus.CONFLICT),
    EXPIRED(4108, HttpStatus.I_AM_A_TEAPOT),
    ;
}
