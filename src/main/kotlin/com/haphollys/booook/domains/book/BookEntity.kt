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
    }

    private fun verifyAvailableDate() {
        val deadline = LocalDateTime.now().minusMinutes(10)

        if (deadline.isAfter(screen.date))
            throw RuntimeException("예약 가능한 시간이 지났습니다.")
    }

    companion object {
        fun of(
            id: Long? = null,
            user: UserEntity,
            screen: ScreenEntity,
            seats: List<Seat>
        ): BookEntity{
            return BookEntity(
                id = id,
                user = user,
                screen = screen,
                seats = seats
            )
        }
    }
}