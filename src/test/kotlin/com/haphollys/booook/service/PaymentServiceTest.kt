package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PaymentDto
import com.haphollys.booook.service.dto.PaymentDto.PaymentRequest
import com.haphollys.booook.service.dto.PaymentDto.UnPaymentRequest
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

    private lateinit var me: UserEntity
    private lateinit var other: UserEntity
    private lateinit var testScreen: ScreenEntity

    private lateinit var myBookEntity: BookEntity
    private val myBookId = 1L
    private lateinit var otherBookEntity: BookEntity
    private val otherBookId = 2L

    private lateinit var userRepository: UserRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var paymentRepository: PaymentRepository

    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        me = UserEntity(id = 1L, name = "ME_USER")
        other = UserEntity(id = 2L, name = "OTHER_USER")
        testScreen = getTestScreenEntity()

        myBookEntity = spyk(
            BookEntity.of(
                id = myBookId,
                user = me,
                screen = testScreen,
                bookedSeats = listOf(
                    BookedSeat(
                        SeatPosition(
                            x = 0,
                            y = 0,
                        ),
                        seatType = FRONT
                    )
                )
            )
        )

        otherBookEntity = BookEntity.of(
            id = otherBookId,
            user = other,
            screen = testScreen,
            bookedSeats = listOf(
                BookedSeat(
                    SeatPosition(
                        x = 1,
                        y = 1,
                    ),
                    seatType = FRONT
                )
            )
        )

        userRepository = mockk()
        bookRepository = mockk()
        paymentRepository = mockk()

        paymentService = PaymentService(
            bookRepository = bookRepository,
            paymentRepository = paymentRepository,
            priceList = PriceList(),
        )
    }

    @Test
    fun `예약된 좌석이 아니면 결제할 수 없다`() {
        every {
            bookRepository.findById(any())
        } returns Optional.empty()

        val notExistsBookId = 1234L

        val paymentRequest = PaymentRequest(
            bookId = notExistsBookId,
            userId = me.id!!
        )

        assertThrows(
            IllegalArgumentException::class.java
        ) { paymentService.pay(paymentRequest = paymentRequest) }
    }

    @Test
    fun `본인이 예약한 좌석이 아니면 Exception`() {
        // given
        val myPaymentRequest = PaymentRequest(
            bookId = otherBookId,
            userId = me.id!!
        )

        every {
            bookRepository.findById(otherBookId)
        } returns Optional.of(otherBookEntity)

        // when, then
        assertThrows(
            IllegalArgumentException::class.java,
        ) { paymentService.pay(paymentRequest = myPaymentRequest) }
    }

    @Test
    fun `결제 테스트`() {
        // given
        every {
            bookRepository.findById(myBookId)
        } returns Optional.of(myBookEntity)

        val myPaymentRequest = PaymentRequest(
            bookId = myBookId,
            userId = me.id!!
        )

        every {
            paymentRepository.save(any())
        } returns PaymentEntity(id = 1L, book = myBookEntity)

        // when
        paymentService.pay(
            paymentRequest = myPaymentRequest
        )

        // then
        verify(atLeast = 1) {
            myBookEntity.pay()
        }

        verify(atLeast = 1) {
            paymentRepository.save(any())
        }
    }

    @Test
    fun `결제 취소`() {
        // given
        val payment = spyk(PaymentEntity(id = 1L, book = myBookEntity))

        val unPaymentRequest = UnPaymentRequest(
            paymentId = payment.id!!,
            userId = me.id!!
        )

        every {
            paymentRepository.findById(1L)
        } returns Optional.of(payment)

        // when
        paymentService.unPay(unPaymentRequest)

        // then
        verify {
            payment.unPay()
        }
    }

    @Test
    fun `본인의 예약이 아니면 결제 취소 시 Exception`() {
        // given
        val otherPayment = PaymentEntity(
            id = 1L,
            book = otherBookEntity,
        )

        val myUnPaymentRequest = UnPaymentRequest(
            paymentId = otherPayment.id!!,
            userId = me.id!!
        )

        every {
            paymentRepository.findById(1L)
        } returns Optional.of(otherPayment)

        // when, then
        assertThrows(
            IllegalArgumentException::class.java,
        ) {
            paymentService.unPay(unPaymentRequest = myUnPaymentRequest)
        }
    }
}