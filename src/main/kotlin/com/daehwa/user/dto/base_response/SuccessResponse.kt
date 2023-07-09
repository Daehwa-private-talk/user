package com.daehwa.user.dto.base_response

class SuccessResponse<T : Any?>(status: Status, result: T) : BaseResponse<T>(true, status, result) {
    companion object {
        private const val OK = "ok"
        val DEFAULT = of(Unit)

        fun <T> of(result: T): SuccessResponse<T> {
            return SuccessResponse(Status(ResponseCode.OK, OK), result)
        }
    }
}
