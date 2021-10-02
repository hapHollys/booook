package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.PaymentDto.*
import com.haphollys.booook.service.external.pg.PGService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PaymentServiceTest {
    val myUserId = 1L
    val otherUserId = 2L

    val myBookId = 3L
    val otherBookId = 4L

    private lateinit var priceList: PriceList

    private lateinit var userRepository: UserRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var paymentRepository: PaymentRepository

    private lateinit var paymentDomainService: PaymentDomainService
    private lateinit var pgService: PGService
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        priceList = PriceList(
            mapOf(
                TWO_D to mapOf(
                    FRONT to 1000,
                    MIDDLE to 2000,
                    BACK to 1000
                )
            )
        )

        userRepository = mockk()
        bookRepository = mockk()
        pgService = mockk(relaxed = true)
        paymentRepository = mockk(relaxed = true)

        paymentDomainService = mockk(relaxed = true)
        paymentService = spyk(
            PaymentService(
                bookRepository = bookRepository,
                paymentRepository = paymentRepository,
                pgService = pgService,
                paymentDomainService = paymentDomainService
            )
        )
    }

    @Test
    fun `예약된 좌석이 아니면 결제할 수 없다`() {
        val notExistsBookId = 1234L

        every {
            bookRepository.findById(notExistsBookId)
        } returns Optional.empty()

        val paymentRequest = PaymentRequest(
            bookId = notExistsBookId,
            userId = 1L
        )

        assertThrows(
            IllegalArgumentException::class.java
        ) { paymentService.pay(paymentRequest = paymentRequest) }
    }

    @Test
    fun `결제 테스트`() {
        // given
        val myBook = mockk<BookEntity>(relaxed = true)
        every {
            myBook.user.id
        } returns myUserId

        every {
            bookRepository.findById(myBookId)
        } returns Optional.of(myBook)

        val myPaymentRequest = PaymentRequest(
            bookId = myBookId,
            userId = myUserId
        )

        every {
            paymentRepository.save(any())
        } returns PaymentEntity(
            id = 1L,
            payerId = myUserId,
            book = myBook
        )

        // when
        paymentService.pay(
            paymentRequest = myPaymentRequest
        )

        // then
        verify(exactly = 1) {
            paymentDomainService.pay(
                payerId = myUserId,
                book = myBook
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
                book = payment.book,
                screen = payment.book.screen
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

        // when
        paymentService.getPaymentList(request)

        // then
        verify {
            paymentRepository.findByUserId(request.userId, any())
        }
    }
}
