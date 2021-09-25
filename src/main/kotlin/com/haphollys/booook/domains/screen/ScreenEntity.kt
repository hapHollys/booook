package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.model.SeatPosition
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
): BaseEntity() {
    fun bookSeats(
        seatPositions: List<SeatPosition>
    ) {
        seatPositions.forEach{
            bookSeat(it)
        }
    }

    private fun bookSeat(
        seatPosition: SeatPosition
    ){
        screenRoom.book(seatPosition)
    }

    fun unBookSeats(
        seatPositions: List<SeatPosition>
    ) {
        seatPositions.forEach{
            unBookSeat(it)
        }
    }

    private fun unBookSeat(
        seatPosition: SeatPosition
    ) {
        screenRoom.unBook(seatPosition)
    }

    fun getBookableSeats(): List<Seat> {
        return this.screenRoom.seats.filter {
            it.bookable()
        }
    }

    fun getSeatNum(): Int {
        return screenRoom.seats.size
    }

    companion object {
        fun of(
            id: Long? = null,
            movie: MovieEntity,
            room: RoomEntity,
            date: LocalDateTime = LocalDateTime.now()
        ): ScreenEntity {
            val seats = ArrayList<Seat>()
            val numRow = room.numRow
            val numCol = room.numCol

            for (row in 0 until numRow) {
                for (col in 0 until numCol) {
                    seats.add(
                        Seat(
                            seatPosition = SeatPosition(
                                x = row,
                                y = col
                            ),
                            seatType = room.getSeatType(row)
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