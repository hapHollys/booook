package com.haphollys.booook.service.dto

import java.time.LocalDateTime


class BookDto() {
    data class BookRequest(
        val userId: Long,
        val screenId: Long,
        val seats: List<SeatDto>
    )

    data class BookResponse(
        val bookId: Long
    )

    class GetBookedListRequest(
        val userId: Long,
        val pagingRequest: PagingRequest? = null
    )

    data class GetBookedResponse(
        val userName: String,
        val bookId: Long,
        val screenId: Long,
        val movieName: String,
        val roomId: Long,
        val roomType: String,
        val bookedSeats: List<SeatDto>,
        val screenDate: LocalDateTime
    )

    data class UnBookRequest(
        val userId: Long,
        val bookId: Long,
    )
}