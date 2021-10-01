package com.haphollys.booook.infra

import com.haphollys.booook.service.external.pg.PGDto
import com.haphollys.booook.service.external.pg.PGService
import org.springframework.stereotype.Component

@Component
class SimplePGServiceImpl : PGService{
    override fun pay(pgPaymentRequest: PGDto.PaymentRequest) {
        TODO("Not yet implemented")
    }

    override fun unPay(pgUnPayRequest: PGDto.UnPaymentRequest) {
        TODO("Not yet implemented")
    }
}