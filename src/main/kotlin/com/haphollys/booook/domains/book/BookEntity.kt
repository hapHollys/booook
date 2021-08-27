package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.book.BookEntity.BookStatus.BOOKED
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
    val seats: List<Seat>,
    val status: BookStatus = BOOKED
) {

    init {
        verifyBook()
    }

    private fun verifyBook() {
        verifyAvailableDate()
    }

    private fun verifyAvailableDate() {
        val deadline = LocalDateTime.now().minusMinutes(10)

        if (deadline.isAfter(screen.date))
            throw RuntimeException("예약 가능한 시간이 지났습니다.")
    }

    enum class BookStatus {
        CANCEL, PAID, BOOKED
    }

    companion object {
        fun of(
            id: Long? = null,
            user: UserEntity,
            screen: ScreenEntity,
            seats: List<Seat>,
            status: BookStatus = BOOKED
        ): BookEntity{
            return BookEntity(
                id = id,
                user = user,
                screen = screen,
                seats = seats,
                status = status
            )
        }
    }
}