package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.model.SeatPosition
import org.hibernate.annotations.BatchSize
import java.lang.IllegalArgumentException
import javax.persistence.*

@Embeddable
class ScreenRoom(
    var roomId: Long,
    var numRow: Int,
    var numCol: Int,
    @Enumerated(value = EnumType.STRING)
    var roomType: RoomType,
    @OneToMany(mappedBy = "screen", cascade = [CascadeType.ALL])
    @BatchSize(size=100)
    var seats: MutableList<SeatEntity> = mutableListOf(),
    var numSeats: Int = numRow * numCol,
    var numRemainSeats: Int = numSeats,
) {
    fun makeSeats(screen: ScreenEntity) {
        for (row in 0 until numRow) {
            for (col in 0 until numCol) {
                seats.add(
                    SeatEntity(
                        screen = screen,
                        seatPosition = SeatPosition(
                            x = row,
                            y = col
                        ),
                        seatType = SeatEntity.SeatType.FRONT
                    )
                )
            }
        }
    }

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

    fun getBookableSeats(): List<SeatEntity> {
        return seats.filter { it.bookable() }
    }

    private fun getSeat(
        seatPosition: SeatPosition
    ): SeatEntity {
        return seats.find { it.seatPosition == seatPosition }?: throw IllegalArgumentException("없는 좌석 입니다.")
    }
}
