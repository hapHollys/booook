package com.haphollys.booook.service

import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.ScreenRepository
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
    private val paymentRepository: PaymentRepository,
    private val pgService: PGService,
    private val screenRepository: ScreenRepository
) {
    fun pay(
        paymentRequest: PaymentRequest
    ): PaymentResponse {
        val payment = paymentDomainService.pay(
            screenRepository = screenRepository,
            payerId = paymentRequest.userId,
            screenId = paymentRequest.screenId,
            seatPositions = paymentRequest.seatPositions.map { it.toSeatPosition() }
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
            payment = payment
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
                screenId = it.screen.id!!,
                movieName = it.screen.movie.name,
                posterImageUrl = "https://www.naver.com",
                bookedSeats = it.bookedSeats.map {
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
