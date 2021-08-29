package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.model.SeatPosition
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class BookedSeat(
    @Embedded
    val seatPosition: SeatPosition,
    @Enumerated(value=EnumType.STRING)
    val seatType: SeatType
) {
}