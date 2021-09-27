package com.haphollys.booook.repository

import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.service.dto.PagingRequest

interface PaymentCustomRepository {
    fun findMyPayments(
        userId: Long,
        pagingRequest: PagingRequest
    ): List<PaymentEntity>
}