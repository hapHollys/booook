package com.haphollys.booook.service.dto


class BookDto() {
    data class BookRequest(
        val screenId: Long,
        val userId: Long,
        val seats: List<SeatDto>
    )

    data class BookResponse(
        val bookId: Long
    )
}