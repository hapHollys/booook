package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.payment.PaymentDomainService
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.domains.room.RoomEntity.RoomType.*
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat
import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PaymentDto
import com.haphollys.booook.service.dto.PaymentDto.*
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
        paymentRepository = mockk(relaxed = true)

        paymentDomainService = mockk(relaxed = true)
        paymentService = spyk(
            PaymentService(
                bookRepository = bookRepository,
                paymentRepository = paymentRepository,
                priceList = priceList,
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
    fun `본인이 예약한 좌석이 아니면 Exception`() {
        // given
        val otherBook = mockk<BookEntity>(relaxed = true)
        every {
            otherBook.user.id
        } returns otherUserId

        every {
            bookRepository.findById(otherBookId)
        } returns Optional.of(otherBook)

        val paymentRequest = PaymentRequest(
            userId = myUserId,
            bookId = otherBookId
        )

        // when, then
        assertThrows(
            IllegalArgumentException::class.java,
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
        verify(atLeast = 1) {
            paymentDomainService.pay(
                payerId = myUserId,
                book = myBook
            )
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

        every {
            paymentService.verifyOwnBook(loginUserId = any(), bookUserId = any())
        } returns Unit

        // when
        paymentService.unPay(unPaymentRequest)

        // then
        verify {
            paymentDomainService.unPay(
                payment = payment,
                book = payment.book,
                screen = payment.book.screen
            )
        }
    }

    @Test
    fun `본인의 예약이 아니면 결제 취소 시 Exception`() {
        // given
        val otherPaymentId = 2L
        val otherPayment = mockk<PaymentEntity>(relaxed = true)

        every {
            otherPayment.book.user.id
        } returns otherUserId

        every {
            paymentRepository.findById(otherPaymentId)
        } returns Optional.of(otherPayment)

        val myUnPaymentRequest = UnPaymentRequest(
            userId = myUserId,
            paymentId = otherPaymentId
        )

        // when, then
        assertThrows(
            IllegalArgumentException::class.java,
        ) {
            paymentService.unPay(unPaymentRequest = myUnPaymentRequest)
        }
    }

    @Test
    fun `결제 내역 조회`() {
        // given
        val request = GetPaymentRequest(userId = myUserId)

        // when
        paymentService.getPaymentList(request)

        // then
        verify {
            paymentRepository.findByPayerId(request.userId)
        }
    }
}