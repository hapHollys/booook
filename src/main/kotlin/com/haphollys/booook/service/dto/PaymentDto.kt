package com.haphollys.booook.service.dto

class PaymentDto() {
    data class PaymentRequest(
        val userId: Long,
        val bookId: Long,
    )

    data class PaymentResponse(
        val paymentId: Long
    )
}