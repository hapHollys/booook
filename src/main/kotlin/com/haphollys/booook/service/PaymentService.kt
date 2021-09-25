package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.service.dto.PaymentDto.*
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(
    private val paymentDomainService: PaymentDomainService,
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
            bookUserId = book.user.id!!
        )

        val payment = paymentDomainService.pay(
            payerId = paymentRequest.userId,
            book = book
        )

        return PaymentResponse(
            paymentId = paymentRepository.save(payment).id!!
        )
    }

    fun unPay(
        unPaymentRequest: UnPaymentRequest
    ): UnPaymentResponse {
        val payment = paymentRepository.findById(unPaymentRequest.paymentId)
            .orElseThrow {
                IllegalArgumentException("해당 결제가 없습니다.")
            }

        verifyOwnBook(
            loginUserId = unPaymentRequest.userId,
            bookUserId = payment.book.user.id!!
        )

        paymentDomainService.unPay(
            payment = payment,
            book = payment.book,
            screen = payment.book.screen
        )

        return UnPaymentResponse(
            paymentId = payment.id!!
        )
    }

    // query option들 처리 방안 고려
    // paging(& sort), where user_id and between A and B
    fun getPaymentList(
        request: GetPaymentRequest
    ): List<GetPaymentResponse> {
        val paymentList = paymentRepository.findMyPayments(
            userId = request.userId,
            pagingRequest = request.pagingRequest
        )

        return paymentList.map {
            GetPaymentResponse(
                paymentId = it.id!!,
                screenId = it.book.screen.id!!,
                movieName = it.book.screen.movie.name,
                posterImageUrl = "https://www.naver.com",
                bookedSeats = it.book.bookedSeats.map {
                    SeatDto(
                        row = it.seatPosition.x,
                        col = it.seatPosition.y,
                        type = it.seatType)
                },
                totalAmount = it.totalAmount!!,
                paymentDate = it.createdAt!!,
                status = it.status
            )
        }
    }

    internal fun verifyOwnBook(
        loginUserId: Long,
        bookUserId: Long
    ) {
        if (bookUserId != loginUserId)
            throw IllegalArgumentException("나의 예약이 아닙니다.")
    }
}