package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.screen.Seat.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.Seat.SeatStatus.FREE
import com.haphollys.booook.model.SeatPosition
import java.lang.RuntimeException
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class Seat(
    @Embedded
    var seatPosition: SeatPosition,
    @Enumerated(value = EnumType.STRING)
    var seatType: SeatType,
    var status: SeatStatus = FREE,
) {

    enum class SeatStatus {
        BOOKED, FREE
    }

    enum class SeatType {
        FRONT, MIDDLE, BACK
    }

    // TODO: ScreenRoom 비교?
    override fun equals(other: Any?): Boolean {
        val o = other as Seat
        return seatPosition == other.seatPosition
    }

    fun book() {
        if (!bookable()) {
            throw RuntimeException("예약할 수 없는 좌석입니다.")
        }

        this.status = BOOKED
    }

    fun bookable(): Boolean {
        return this.status == FREE
    }
}
