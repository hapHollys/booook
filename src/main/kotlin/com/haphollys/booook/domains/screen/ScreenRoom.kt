package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.*
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.model.SeatPosition
import java.math.BigDecimal
import javax.persistence.*

@Embeddable
class ScreenRoom(
    var roomId: Long,
    var numRow: Int,
    var numCol: Int,
    @Enumerated(value = EnumType.STRING)
    var roomType: RoomType,
    @OneToMany(mappedBy = "screen", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seats: MutableList<SeatEntity> = mutableListOf(),
    var numSeats: Int = numRow * numCol,
    var numRemainSeats: Int = numSeats,
) {
    fun makeSeats(
        screen: ScreenEntity,
        priceMap: Map<RoomType, Map<SeatEntity.SeatType, BigDecimal>>
    ) {
        for (row in 0 until numRow) {
            for (col in 0 until numCol) {
                seats.add(
                    SeatEntity(
                        screen = screen,
                        seatPosition = SeatPosition(
                            x = row,
                            y = col
                        ),
                        seatType = SeatEntity.SeatType.FRONT,
                        price = priceMap[roomType]!![SeatEntity.SeatType.FRONT]!!
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

    fun getSeat(
        seatPosition: SeatPosition
    ): SeatEntity {
        var seatEntity: SeatEntity? = null
        seats.forEach {
            if (it.seatPosition == seatPosition) {
                seatEntity = it
            }
        }
        return seatEntity!!
    }
}
