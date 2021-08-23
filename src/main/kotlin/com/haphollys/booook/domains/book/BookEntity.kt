package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.room.Seat
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "books")
@Entity
class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne
    val user: UserEntity,
    @OneToOne
    val screen: ScreenEntity,
    @ElementCollection
    val seats: List<Seat>
) {

    init {
        verifyBook()
    }

    private fun verifyBook() {
        verifyAvailableDate()
        verifyAvailableSeat()
    }

    private fun verifyAvailableDate() {
        val deadline = LocalDateTime.now().minusMinutes(10)

        if (deadline.isAfter(screen.date))
            throw RuntimeException("예약 가능한 시간이 지났습니다.")
    }

    private fun verifyAvailableSeat() {
        seats.forEach { bookedSeat ->
            screen.room.seats
                .forEach { seat ->
                    if (seat.equals(bookedSeat) && seat.status == Seat.SeatStatus.BOOKED) {
                        throw RuntimeException("이미 예약된 좌석입니다.")
                    }
                }
        }
    }

    companion object {
        fun of(
            user: UserEntity,
            screen: ScreenEntity,
            seats: List<Seat>
        ): BookEntity{
            return BookEntity(
                user = user,
                screen = screen,
                seats = seats)
        }
    }
}