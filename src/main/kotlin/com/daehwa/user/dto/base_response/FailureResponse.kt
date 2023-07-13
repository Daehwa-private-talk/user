package com.daehwa.user.dto.base_response

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

class FailureResponse(val status: Status) : DaehwaResponse {
    val success = false

    @Throws(JsonProcessingException::class)
    fun convertToJson(): String {
        val mapper = ObjectMapper()

        return mapper.writeValueAsString(this)
    }
}
