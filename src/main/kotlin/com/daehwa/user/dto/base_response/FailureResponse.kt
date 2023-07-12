package com.daehwa.user.dto.base_response

class FailureResponse(val status: Status) : DaehwaResponse {
    val success = false
}
