package com.daehwa.user.config

import com.daehwa.user.common.exception.DaehwaException
import com.daehwa.user.dto.base_response.FailureResponse
import com.daehwa.user.dto.base_response.Status
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: RuntimeException) {
            setErrorResponse(response, e)
        }
    }

    private fun setErrorResponse(response: HttpServletResponse, e: RuntimeException) {
        e as DaehwaException
        val status = Status(e.errorCode.responseCode, e.message ?: "")
        val failureResponse = FailureResponse(status)
        response.status = e.errorCode.responseCode.httpStatus.value()
        response.contentType = "application/json; charset=utf8"

        try {
            val json: String = failureResponse.convertToJson()
            response.writer.write(json)
        } catch (e: IOException) {
            logger.error("Error response 처리 도중 에러 발생")
        }
    }
}
