package com.haphollys.booook.service.external.pg

import com.haphollys.booook.service.external.pg.PGDto.PaymentRequest
import com.haphollys.booook.service.external.pg.PGDto.UnPaymentRequest

interface PGService {
    fun pay(pgPaymentRequest: PaymentRequest)

    fun unPay(pgUnPayRequest: UnPaymentRequest)
}
