package com.haphollys.booook.repository

import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.payment.QPaymentEntity.paymentEntity
import com.haphollys.booook.service.dto.PagingRequest
import com.querydsl.jpa.impl.JPAQueryFactory

class PaymentCustomRepositoryImpl(
    private val query: JPAQueryFactory
): PaymentCustomRepository {
    override fun findMyPayments(
        userId: Long,
        pagingRequest: PagingRequest
    ): List<PaymentEntity> {
        return query.select(paymentEntity)
            .from(paymentEntity)
            .where(paymentEntity.payerId.eq(userId))
            .fetch()
    }
}