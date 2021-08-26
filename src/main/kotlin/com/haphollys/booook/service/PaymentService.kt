package com.haphollys.booook.service

import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PaymentDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
@Transactional
class PaymentService(
        private val userRepository: UserRepository,
        private val bookRepository: BookRepository,
) {
    fun pay(paymentRequest: PaymentDto.PaymentRequest) {
        val book = bookRepository.findById(paymentRequest.bookId)
                .orElseThrow{
                    throw IllegalArgumentException("해당 예약이 없습니다.")
                }
    }

}