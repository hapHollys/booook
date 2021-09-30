package com.haphollys.booook.repository

import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.payment.QPaymentEntity.paymentEntity
import com.haphollys.booook.service.dto.PagingRequest
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory

class PaymentCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : PaymentCustomRepository {
    override fun findMyPayments(
        userId: Long,
        pagingRequest: PagingRequest
    ): List<PaymentEntity> {
        return query.select(paymentEntity)
            .from(paymentEntity)
            .where(payerIdEq(userId), cursorPosition(pagingRequest.lastId))
            .limit(pagingRequest.size)
            .fetch()
    }

    private fun payerIdEq(
        userId: Long
    ): Predicate {
        return paymentEntity.payerId.eq(userId)
    }

    private fun cursorPosition(lastId: Long?): Predicate? {
        if (lastId == null) return null

        return paymentEntity.id.lt(lastId)
    }
}
