package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentEntity.Status.CANCEL
import com.haphollys.booook.domains.payment.PaymentEntity.Status.PAID
import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.domains.screen.Seat.SeatType
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.EnumType.STRING

@Table(name = "payments")
@Entity
class PaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var payerId: Long,
    @OneToOne
    var book: BookEntity,
    var totalAmount: Int? = null,
    @Enumerated(value = STRING)
    var status: Status = PAID,
    var canceledAt: LocalDateTime? = null
) : BaseEntity() {
    enum class Status {
        PAID, CANCEL
    }

    internal fun setTotalAmount(priceList: Map<RoomType, Map<SeatType, Int>>) {
        val roomType = book.screen.screenRoom.roomType

        this.totalAmount = book.bookedSeats
            .sumOf { priceList[roomType]!![it.seatType]!! }
    }

    fun unPay() {
        verifyUnPayable()

        this.status = CANCEL
        this.canceledAt = LocalDateTime.now()
    }

    internal fun verifyUnPayable() {
        if (status != PAID) {
            throw IllegalArgumentException("취소 가능한 상태가 아닙니다")
        }
    }

    companion object {
        fun of(
            payerId: Long,
            book: BookEntity,
            priceList: Map<RoomType, Map<SeatType, Int>>
        ): PaymentEntity {
            val payment = PaymentEntity(
                payerId = payerId,
                book = book
            )
            payment.setTotalAmount(priceList)
            return payment
        }
    }
}
