package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.model.SeatPosition
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "screens")
@Entity
class ScreenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY)
    var movie: MovieEntity,
    @Embedded
    var screenRoom: ScreenRoom,
    var date: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {
    fun getDeadline(): LocalDateTime {
        return date.minusMinutes(BOOK_DEADLINE_MINUTES)
    }

    fun bookSeats(
        seatPositions: List<SeatPosition>
    ) {
        screenRoom.book(seatPositions)
    }

    fun unBookSeats(
        seatPositions: List<SeatPosition>
    ) {
        screenRoom.unBook(seatPositions)
    }

    fun getBookableSeats(): List<Seat> {
        return this.screenRoom.getBookableSeats()
    }

    fun getSeat(seatPosition: SeatPosition): Seat {
        return screenRoom.getSeat(seatPosition)
    }

    fun getNumSeats(): Int {
        return screenRoom.numSeats
    }

    fun getNumRemainSeats(): Int {
        return screenRoom.numRemainSeats
    }

    companion object {
        const val BOOK_DEADLINE_MINUTES = 10L

        fun of(
            id: Long? = null,
            movie: MovieEntity,
            room: RoomEntity,
            date: LocalDateTime = LocalDateTime.now(),
            priceTable:  Map<RoomEntity.RoomType, Map<Seat.SeatType, BigDecimal>>
        ): ScreenEntity {
            val seats = ArrayList<Seat>()
            val numRow = room.numRow
            val numCol = room.numCol

            for (row in 0 until numRow) {
                val seatType = room.getSeatType(row)
                for (col in 0 until numCol) {
                    seats.add(
                        Seat(
                            seatPosition = SeatPosition(
                                x = row,
                                y = col
                            ),
                            seatType = seatType,
                            price = priceTable[room.roomType]!![seatType]!!
                        )
                    )
                }
            }

            val screenRoom = ScreenRoom(
                roomId = room.id!!,
                numRow = numRow,
                numCol = numCol,
                seats = seats,
                roomType = room.roomType
            )

            return ScreenEntity(
                id = id,
                movie = movie,
                screenRoom = screenRoom,
                date = date
            )
        }
    }
}
