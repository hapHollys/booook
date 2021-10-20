package com.haphollys.booook.service

import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.service.dto.PaymentDto.*
import com.haphollys.booook.service.dto.SeatDto
import com.haphollys.booook.service.external.pg.PGDto
import com.haphollys.booook.service.external.pg.PGService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class PaymentService(
    private val paymentDomainService: PaymentDomainService,
    private val bookRepository: BookRepository,
    private val paymentRepository: PaymentRepository,
    private val pgService: PGService
) {
    fun pay(
        paymentRequest: PaymentRequest
    ): PaymentResponse {
        val book = bookRepository.findById(paymentRequest.bookId)
            .orElseThrow {
                EntityNotFoundException("해당 예약이 없습니다.")
            }

        val payment = paymentDomainService.pay(
            payerId = paymentRequest.userId,
            book = book
        )

        pgService.pay(
            PGDto.PaymentRequest(
                paymentId = payment.id!!,
                amount = payment.totalAmount!!
            )
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
                EntityNotFoundException("해당 결제가 없습니다.")
            }

        paymentDomainService.unPay(
            userId = unPaymentRequest.userId,
            payment = payment,
            book = payment.book,
            screen = payment.book.screen
        )

        pgService.unPay(
            pgUnPayRequest = PGDto.UnPaymentRequest(payment.id!!)
        )

        return UnPaymentResponse(
            paymentId = payment.id!!
        )
    }

    // query option들 처리 방안 고려
    // paging(& sort), where user_id and between A and B
    @Transactional(readOnly = true)
    fun getPaymentList(
        request: GetPaymentRequest
    ): List<GetPaymentResponse> {
        val paymentList = paymentRepository.findByUserId(
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
                        type = it.seatType
                    )
                },
                totalAmount = it.totalAmount!!,
                paymentDate = it.createdAt!!,
                status = it.status
            )
        }
    }
}
