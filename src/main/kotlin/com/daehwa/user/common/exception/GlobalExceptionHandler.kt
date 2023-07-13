package com.daehwa.user.common.exception

import com.daehwa.user.dto.base_response.FailureResponse
import com.daehwa.user.dto.base_response.ResponseCode
import com.daehwa.user.dto.base_response.Status
import com.daehwa.user.utils.HttpServletUtils
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val logger = KotlinLogging.logger { }
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
    }

    @ExceptionHandler(DaehwaException::class)
    fun handleDaehwaException(e: DaehwaException): ResponseEntity<FailureResponse> {
        log(e)

        val status = Status(e.errorCode.responseCode, e.message ?: UNKNOWN_ERROR_MESSAGE)

        return ResponseEntity.status(e.errorCode.responseCode.httpStatus)
            .body(FailureResponse(status))
    }

    @ExceptionHandler(Exception::class)
    fun unhandledException(e: Exception, req: HttpServletRequest): ResponseEntity<FailureResponse> {
        logger.error("URL [${HttpServletUtils.getFullURL(req)}]", e)

        val status = Status(ResponseCode.UNKNOWN, UNKNOWN_ERROR_MESSAGE)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(FailureResponse(status))
    }

    private fun log(e: DaehwaException) =
        when (e.errorCode.logLevel) {
            LogLevel.TRACE -> logger.trace(e.message, e)
            LogLevel.DEBUG -> logger.debug(e.message, e)
            LogLevel.INFO -> logger.info(e.message, e)
            LogLevel.WARN -> logger.warn(e.message, e)
            LogLevel.ERROR -> logger.error(e.message, e)
            else -> logger.error(e.message, e)
        }

}
