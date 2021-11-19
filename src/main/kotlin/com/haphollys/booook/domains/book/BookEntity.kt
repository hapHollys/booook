package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.book.BookEntity.BookStatus.*
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.EnumType.STRING

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
    @ElementCollection
    @CollectionTable(
        name = "booked_seats",
        // 유일성 보장을 위해 꼭 필요
        uniqueConstraints = [UniqueConstraint(name = "booked_seat_unique_idx", columnNames = ["screen_id", "x", "y"])]
    )
    var bookedSeats: MutableList<BookedSeat>,
    @Enumerated(value = STRING)
    var status: BookStatus = BOOKED
) : BaseEntity() {
    fun unBook() {
        verifyUnBookableStatus()

        this.status = CANCEL
    }

    fun pay() {
        verifyPayableStatus()

        this.status = PAID
    }

    private fun verifyUnBookableStatus() {
        if (this.status != BOOKED)
            throw IllegalStateException("예약 취소 가능한 상태가 아닙니다.")
    }

    private fun verifyPayableStatus() {
        if (status != BOOKED)
            throw IllegalArgumentException("결제 가능한 상태가 아닙니다.")
    }

    enum class BookStatus {
        CANCEL, PAID, BOOKED
    }

    companion object {
        fun of(
            id: Long? = null,
            user: UserEntity,
            screen: ScreenEntity,
            bookedSeats: MutableList<BookedSeat>,
            status: BookStatus = BOOKED,
        ): BookEntity {
           screen.verifyBookableTime(LocalDateTime.now())

            return BookEntity(
                id = id,
                user = user,
                screen = screen,
                bookedSeats = bookedSeats,
                status = status,
            )
        }
    }
}
