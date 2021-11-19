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

    fun getBookableSeats(): List<SeatEntity> {
        return this.screenRoom.getBookableSeats()
    }

    fun getNumSeats(): Int {
        return screenRoom.numSeats
    }

    fun getNumRemainSeats(): Int {
        return screenRoom.numRemainSeats
    }

    fun verifyBookableTime(bookedAt: LocalDateTime) {
        val deadline = date.minusMinutes(BOOK_DEADLINE_MINUTES)
        if (bookedAt.isAfter(deadline))
            throw IllegalArgumentException("예매 가능한 시간이 아닙니다.")
    }

    companion object {
        const val BOOK_DEADLINE_MINUTES = 10L

        fun of(
            id: Long? = null,
            movie: MovieEntity,
            room: RoomEntity,
            date: LocalDateTime = LocalDateTime.now()
        ): ScreenEntity {
            val screenRoom = ScreenRoom(
                roomId = room.id!!,
                numRow = room.numRow,
                numCol = room.numCol,
                roomType = room.roomType
            )

            val screen = ScreenEntity(
                id = id,
                movie = movie,
                date = date,
                screenRoom = screenRoom
            )
            screenRoom.makeSeats(screen)

            return screen
        }
    }
}
