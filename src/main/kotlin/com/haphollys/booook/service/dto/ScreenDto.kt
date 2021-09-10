package com.haphollys.booook.service.dto

class ScreenDto {
    data class GetBookableSeatsRequest(
        val screenId: Long,
    )

    data class GetBookableSeatsResponse(
        val seats: List<SeatDto>
    )
}