package com.haphollys.booook.service.dto

import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.model.SeatPosition

data class SeatDto(
    val row: Int,
    val col: Int,
    val type: SeatType
) {
    fun toSeatPosition(): SeatPosition {
        return SeatPosition(
            x = row,
            y = col
        )
    }
}
