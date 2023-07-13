package com.daehwa.user.common.exception

import com.daehwa.user.dto.base_response.ResponseCode
import org.springframework.boot.logging.LogLevel

enum class ErrorCode(val responseCode: ResponseCode, val logLevel: LogLevel) {
    DUPLICATED(ResponseCode.DUPLICATED, LogLevel.DEBUG),
    UNAUTHORIZED(ResponseCode.UNAUTHORIZED, LogLevel.WARN),
    FORBIDDEN(ResponseCode.FORBIDDEN, LogLevel.WARN),
    NOT_FOUND(ResponseCode.NOT_FOUND, LogLevel.WARN),
    INVALID_PARAMETER(ResponseCode.INVALID_PARAMETER, LogLevel.DEBUG),
    ILLEGAL_STATE(ResponseCode.ILLEGAL_STATE, LogLevel.DEBUG),
    UNSUPPORTED(ResponseCode.UNSUPPORTED, LogLevel.DEBUG),
    UNREACHABLE(ResponseCode.UNKNOWN, LogLevel.DEBUG),
    UNKNOWN(ResponseCode.UNKNOWN, LogLevel.ERROR),
    BAD_REQUEST(ResponseCode.BAD_REQUEST, LogLevel.DEBUG),
    GONE(ResponseCode.GONE, LogLevel.DEBUG),
    DUPLICATED_LOGIN(ResponseCode.DUPLICATED_LOGIN, LogLevel.DEBUG),
    EXPIRED(ResponseCode.EXPIRED, LogLevel.WARN)
    ;
}
