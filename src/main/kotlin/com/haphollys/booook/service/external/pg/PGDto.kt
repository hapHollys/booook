package com.haphollys.booook.service.external.pg

class PGDto {
    data class PaymentRequest(
        val paymentId: Long,
        val amount: Int
    )

    data class UnPaymentRequest(
        val paymentId: Long
    )
}
