package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.model.SeatPosition
import org.hibernate.annotations.BatchSize
import javax.persistence.*

@Embeddable
class ScreenRoom(
    var roomId: Long,
    var numRow: Int,
    var numCol: Int,
    @Enumerated(value = EnumType.STRING)
    var roomType: RoomType,
    @ElementCollection
    @BatchSize(size=30)
    var seats: MutableList<Seat>,
    var numSeats: Int = numRow * numCol,
) {
    fun book(bookSeatPosition: SeatPosition) {
        getSeat(bookSeatPosition).book()
    }

    fun unBook(bookSeatPosition: SeatPosition) {
        getSeat(bookSeatPosition).unBook()
    }

    fun getSeatsSize(): Int {
        return numRow * numCol
    }

    fun getBookableSeats(): List<Seat> {
        return seats.filter { it.bookable() }
    }

    private fun getSeat(
        seatPosition: SeatPosition
    ): Seat {
        var seat: Seat? = null
        seats.forEach {
            if (it.seatPosition == seatPosition) {
                seat = it
            }
        }
        return seat!!
    }
}
