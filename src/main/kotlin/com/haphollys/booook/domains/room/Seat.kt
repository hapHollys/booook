package com.haphollys.booook.domains.room

import com.haphollys.booook.domains.room.Seat.SeatStatus.BOOKED
import com.haphollys.booook.domains.room.Seat.SeatStatus.FREE
import java.lang.RuntimeException
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToOne

@Embeddable
class Seat(
    @OneToOne
    val room: RoomEntity,
    val row: Int,
    val col: Int,
    @Enumerated(value = EnumType.STRING)
    val seatType: SeatType,
    var status: SeatStatus = FREE,
) {

    enum class SeatStatus {
        BOOKED, FREE
    }

    enum class SeatType {
        FRONT, MIDDLE, BACK
    }

    override fun equals(other: Any?): Boolean {
        val o = other as Seat
        return room.equals(o.room) && row == o.row && col == o.col
    }

    fun book() {
        if (!bookable()) {
            throw RuntimeException("예약할 수 없는 좌석입니다.")
        }

        this.status = BOOKED
    }

    fun bookable(): Boolean {
        return this.status != FREE
    }
}
