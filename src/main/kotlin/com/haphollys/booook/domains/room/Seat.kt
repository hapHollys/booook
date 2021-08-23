package com.haphollys.booook.domains.room

import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToOne

@Embeddable
class Seat(
    @OneToOne
    var room: RoomEntity? = null,
    val row: Int,
    val col: Int,
    val status: SeatStatus,
    @Enumerated(value = EnumType.STRING)
    val seatType: SeatType
) {

    enum class SeatStatus {
        BOOK, FREE
    }

    enum class SeatType {
        FRONT, MIDDLE, BACK
    }
}

