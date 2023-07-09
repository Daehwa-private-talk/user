package com.daehwa.user.dto.base_response

class FailureResponse<T : Any?>(status: Status) : BaseResponse<T>(false, status, null) {
}
