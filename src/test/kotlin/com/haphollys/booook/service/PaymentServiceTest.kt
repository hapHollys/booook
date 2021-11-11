package com.haphollys.booook.service

import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.getTestPriceList
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.PaymentDto.*
import com.haphollys.booook.service.external.pg.PGService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PaymentServiceTest {
    val myUserId = 1L

    private lateinit var priceList: PriceList

    private lateinit var userRepository: UserRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var screenRepository: ScreenRepository

    private lateinit var paymentDomainService: PaymentDomainService
    private lateinit var pgService: PGService
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        priceList = getTestPriceList()

        userRepository = mockk()
        pgService = mockk(relaxed = true)
        paymentRepository = mockk(relaxed = true)
        screenRepository = mockk(relaxed = true)

        paymentDomainService = mockk(relaxed = true)
        paymentService = PaymentService(
            paymentRepository = paymentRepository,
            screenRepository = screenRepository,
            pgService = pgService,
            paymentDomainService = paymentDomainService,
        )
    }

    @Test
    fun `결제 테스트`() {
        // given
        val myPaymentRequest = PaymentRequest(
            userId = myUserId,
            screenId = 1L,
            seatPositions = mutableListOf()
        )

        every {
            paymentRepository.save(any())
        } returns mockk(relaxed = true)

        // when
        paymentService.pay(
            paymentRequest = myPaymentRequest
        )

        // then
        verify(exactly = 1) {
            paymentDomainService.pay(
                screenRepository = screenRepository,
                payerId = myUserId,
                screenId = any(),
                seatPositions = any()
            )
        }

        verify(exactly = 1) {
            pgService.pay(any())
        }

        verify(atLeast = 1) {
            paymentRepository.save(any())
        }
    }

    @Test
    fun `결제 취소`() {
        // given
        val payment = mockk<PaymentEntity>(relaxed = true)

        val unPaymentRequest = UnPaymentRequest(
            paymentId = payment.id!!,
            userId = myUserId
        )

        every {
            paymentRepository.findById(any())
        } returns Optional.of(payment)

        // when
        paymentService.unPay(unPaymentRequest)

        // then
        verify {
            paymentDomainService.unPay(
                userId = myUserId,
                payment = payment,
            )
        }

        verify(exactly = 1) {
            pgService.unPay(any())
        }
    }

    @Test
    fun `결제 내역 조회`() {
        // given
        val request = GetPaymentRequest(userId = myUserId, PagingRequest())

        every {
            paymentRepository.findByUserId(
                userId = request.userId,
                pagingRequest = any()
            )
        } returns listOf(
            mockk(relaxed = true)
        )

        // when
        paymentService.getPaymentList(request)

        // then
        verify {
            paymentRepository.findByUserId(request.userId, any())
        }
    }
}
