package com.haphollys.booook.service.external.pg

import java.math.BigDecimal

class PGDto {
    data class PaymentRequest(
        val paymentId: Long,
        val amount: BigDecimal
    )

    data class UnPaymentRequest(
        val paymentId: Long
    )
}
