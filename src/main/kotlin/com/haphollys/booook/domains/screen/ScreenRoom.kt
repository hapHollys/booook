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
    var numRemainSeats: Int = numSeats,
) {
    fun book(bookSeatPositions: List<SeatPosition>) {
        bookSeatPositions.forEach {
            getSeat(it).book()
        }

        numRemainSeats -= bookSeatPositions.size
    }

    fun unBook(bookSeatPositions: List<SeatPosition>) {
        bookSeatPositions.forEach {
            getSeat(it).unBook()
        }

        numRemainSeats += bookSeatPositions.size
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
