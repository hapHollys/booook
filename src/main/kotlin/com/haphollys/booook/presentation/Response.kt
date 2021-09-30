package com.haphollys.booook.presentation

data class Response<T>(
    val data: T,
    val status: Int = 200,
    val message: String = "OK",
    val metadata: Any? = null
)
