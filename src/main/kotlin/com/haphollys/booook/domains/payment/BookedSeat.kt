package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.model.SeatPosition
import java.math.BigDecimal
import javax.persistence.Embeddable

@Embeddable
class BookedSeat(
    var seatPosition: SeatPosition,
    var seatType: SeatType,
    var price: BigDecimal
)