package com.haphollys.booook.repository

import com.haphollys.booook.domains.payment.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository: JpaRepository<PaymentEntity, Long> {
    fun findByPayerId(userId: Long): List<PaymentEntity>
}