package com.haphollys.booook.service.dto

data class PagingRequest(
    val size: Int = DEFAULT_PAGING_SIZE,
    val lastId: Long? = null,
)

const val DEFAULT_PAGING_SIZE = 20