package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.book.BookEntity.BookStatus.BOOKED
import com.haphollys.booook.domains.book.BookEntity.BookStatus.CANCEL
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.model.SeatPosition
import java.lang.IllegalStateException
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "books")
@Entity
class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne
    var user: UserEntity,
    @OneToOne
    var screen: ScreenEntity,
    var bookedAt: LocalDateTime = LocalDateTime.now(),
    @ElementCollection
    var bookedSeats: List<BookedSeat>,
    var status: BookStatus = BOOKED
) {
    init {
        verifyBook()
    }

    fun unBook() {
        verifyUnBookableStatus()

        bookedSeats.forEach {
            this.screen.screenRoom.getSeat(it.seatPosition).unBook()
        }

        this.status = CANCEL
    }

    private fun verifyUnBookableStatus() {
        if (this.status != BOOKED)
            throw IllegalStateException("예약 취소 가능한 상태가 아닙니다.")
    }

    private fun verifyBook() {
        verifyAvailableDate()
    }

    private fun verifyAvailableDate() {
        val deadline = LocalDateTime.now().minusMinutes(10)

        if (deadline.isAfter(screen.date))
            throw RuntimeException("예약 가능한 시간이 지났습니다.")
    }

    override fun toString(): String {
        return "BookEntity(id=$id, bookedAt=$bookedAt, bookedSeats=$bookedSeats, status=$status)"
    }

    enum class BookStatus {
        CANCEL, PAID, BOOKED
    }

    companion object {
        fun of(
            id: Long? = null,
            user: UserEntity,
            screen: ScreenEntity,
            bookedSeats: List<BookedSeat>,
            status: BookStatus = BOOKED,
            date: LocalDateTime = LocalDateTime.now().minusMinutes(6)
        ): BookEntity{
            screen.bookSeats(bookedSeats.map { it.seatPosition })

            return BookEntity(
                id = id,
                user = user,
                screen = screen,
                bookedSeats = bookedSeats,
                status = status,
                bookedAt = date
            )
        }
    }


}