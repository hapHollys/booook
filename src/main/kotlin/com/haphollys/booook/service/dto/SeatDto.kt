package com.haphollys.booook.service.dto

import com.haphollys.booook.domains.room.Seat.SeatType

data class SeatDto(
    val row: Int,
    val col: Int,
    val type: SeatType
)