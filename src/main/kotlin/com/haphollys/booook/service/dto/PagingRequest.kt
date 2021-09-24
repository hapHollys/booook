package com.haphollys.booook.service.dto

data class PagingRequest(
    val size: Int,
    val lastId: Long,
)