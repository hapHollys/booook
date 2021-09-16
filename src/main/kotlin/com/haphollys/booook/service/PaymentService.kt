package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.service.dto.PaymentDto.PaymentRequest
import com.haphollys.booook.service.dto.PaymentDto.PaymentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(
    private val bookRepository: BookRepository,
    private val paymentRepository: PaymentRepository,
    private val priceList: PriceList,
) {
    fun pay(
        paymentRequest: PaymentRequest
    ): PaymentResponse {
        val book = bookRepository.findById(paymentRequest.bookId)
            .orElseThrow {
                IllegalArgumentException("해당 예약이 없습니다.")
            }

        verifyOwnBook(
            loginUserId = paymentRequest.userId,
            book = book
        )

        book.pay()
        val paymentEntity = PaymentEntity.of(book, priceList.table)

        return PaymentResponse(
            paymentId = paymentRepository.save(paymentEntity).id!!
        )
    }

    internal fun verifyOwnBook(
        loginUserId: Long,
        book: BookEntity
    ) {
        if (book.user.id != loginUserId)
            throw IllegalArgumentException("나의 예약이 아닙니다.")
    }

    // TODO : 결제 내역 조회, 결제 취소
}