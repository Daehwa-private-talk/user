package com.daehwa.user.model.base_response

class FailureResponse<T : Any?>(status: Status) : BaseResponse<T>(false, status, null) {
}
