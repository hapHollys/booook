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
    var bookedAt: LocalDateTime = LocalDateTime.now(),
    @ElementCollection
    var bookedSeats: MutableList<BookedSeat>,
    @Enumerated(value = STRING)
    var status: BookStatus = BOOKED
) : BaseEntity() {
    init {
        verifyBook()
    }

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

    private fun verifyBook() {
        verifyAvailableDate()
    }

    private fun verifyAvailableDate() {
        if (bookedAt.isAfter(screen.getDeadline()))
            throw RuntimeException("예약 가능한 시간이 지났습니다.")
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
