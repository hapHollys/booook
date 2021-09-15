package com.haphollys.booook.domains.screen

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
) {
    fun bookSeats(
        bookSeats: List<SeatPosition>
    ) {
        bookSeats.forEach{
            bookSeat(it)
        }
    }

    private fun bookSeat(
        bookSeatPosition: SeatPosition
    ){
        screenRoom.book(bookSeatPosition)
    }

    fun unBookSeats(
        bookSeats: List<SeatPosition>
    ) {
        bookSeats.forEach{
            unBookSeat(it)
        }
    }

    private fun unBookSeat(bookSeatPosition: SeatPosition) {
        screenRoom.unBook(bookSeatPosition)
    }

    fun getBookableSeats(): List<Seat> {
        return this.screenRoom.seats.filter {
            it.bookable()
        }
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