package com.haphollys.booook.service.dto

import com.haphollys.booook.domains.payment.PaymentEntity
import java.math.BigDecimal
import java.time.LocalDateTime

class PaymentDto() {
    data class PaymentRequest(
        val userId: Long,
        val screenId: Long,
        val seatPositions: List<SeatDto>
    )

    data class PaymentResponse(
        val paymentId: Long
    )

    data class UnPaymentRequest(
        val userId: Long,
        val paymentId: Long,
    )

    data class UnPaymentResponse(
        val paymentId: Long,
    )

    data class GetPaymentRequest(
        val userId: Long,
        val pagingRequest: PagingRequest
    )

    data class GetPaymentResponse(
        val paymentId: Long,
        val screenId: Long,
        val movieName: String,
        val posterImageUrl: String,
        val bookedSeats: List<SeatDto>,
        val totalAmount: BigDecimal,
        val paymentDate: LocalDateTime,
        val status: PaymentEntity.Status
    )
}
