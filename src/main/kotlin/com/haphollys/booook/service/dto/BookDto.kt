package com.haphollys.booook.service.dto

import java.time.LocalDateTime


class BookDto() {
    data class BookRequest(
        val screenId: Long,
        val userId: Long,
        val seats: List<SeatDto>
    )

    data class BookResponse(
        val bookId: Long
    )

    data class GetBookedListRequest(
        val userId: Long
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
        val bookId: Long,
        val userId: Long
    )
}