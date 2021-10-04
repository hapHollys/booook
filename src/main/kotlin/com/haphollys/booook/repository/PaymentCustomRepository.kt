package com.haphollys.booook.repository

import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.service.dto.PagingRequest

interface PaymentCustomRepository {
    fun findByUserId(
        userId: Long,
        pagingRequest: PagingRequest
    ): List<PaymentEntity>
}
