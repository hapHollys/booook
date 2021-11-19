package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.SeatEntity.SeatType
import com.haphollys.booook.model.SeatPosition
import javax.persistence.*

@Embeddable
class BookedSeat(
    @Column(name = "screen_id")
    var screenId: Long,
    @Embedded
    var seatPosition: SeatPosition,
    @Enumerated(value = EnumType.STRING)
    var seatType: SeatType
)
