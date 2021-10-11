package com.haphollys.booook.presentation

data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = "",
    val metadata: Any? = null
) {
    companion object {
        fun error(
            message: String?
        ): ApiResponse<Unit> {
            return ApiResponse(
                message = message
            )
        }

        fun <T> success(
            data: T,
        ): ApiResponse<T> {
            return ApiResponse(
                data = data
            )
        }
    }
}
