package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.payment.PaymentEntity.Status.CANCEL
import com.haphollys.booook.domains.payment.PaymentEntity.Status.PAID
import com.haphollys.booook.domains.screen.ScreenEntity
import java.math.BigDecimal
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
    @OneToOne(fetch = FetchType.LAZY)
    var screen: ScreenEntity,
    @ElementCollection
    @CollectionTable(
        name = "booked_seats",
        joinColumns = [JoinColumn(name = "screen_id", referencedColumnName = "id", table = "screens")]
    )
    var bookedSeats: MutableList<BookedSeat> = ArrayList(),
    var totalAmount: BigDecimal? = null,
    @Enumerated(value = STRING)
    var status: Status = PAID,
    var canceledAt: LocalDateTime? = null
) : BaseEntity() {
    enum class Status {
        PAID, CANCEL
    }

    internal fun setTotalAmount() {
        this.totalAmount = bookedSeats.sumOf { it.price }
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
            screen: ScreenEntity,
            bookedSeats: MutableList<BookedSeat>
        ): PaymentEntity {
            val payment = PaymentEntity(
                payerId = payerId,
                screen = screen,
                bookedSeats = bookedSeats
            )
            payment.setTotalAmount()
            return payment
        }
    }
}
