package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.domains.room.Seat
import com.haphollys.booook.domains.room.Seat.SeatType.FRONT
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto
import com.haphollys.booook.service.dto.PaymentDto
import com.haphollys.booook.service.dto.PaymentDto.*
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PaymentServiceTest {

    private lateinit var me: UserEntity
    private lateinit var other: UserEntity
    private lateinit var testScreen: ScreenEntity

    private lateinit var userRepository: UserRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var paymentRepository: PaymentRepository

    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        me = UserEntity(id = 1L, name = "ME_USER")
        other = UserEntity(id = 2L, name = "OTHER_USER")
        testScreen = getTestScreenEntity()

        userRepository = mockk()
        bookRepository = mockk()
        paymentRepository = mockk()

        paymentService = PaymentService(
            bookRepository = bookRepository,
            paymentRepository = paymentRepository
        )
    }

    @Test
    fun `예약된 좌석이 아니면 결제할 수 있다`() {
        every {
            bookRepository.findById(any())
        } returns Optional.empty()

        val notExistsBookId = 1234L

        val paymentRequest = PaymentRequest(
            bookId = notExistsBookId,
        )

        assertThrows(
            IllegalArgumentException::class.java
        ) { paymentService.pay(loginUser = me, paymentRequest = paymentRequest) }
    }

    @Test
    fun `본인이 예약한 좌석이 아니면 Exception`() {
        // given
        val othersBookId = 1L

        val othersBookEntity = BookEntity.of(
            id = othersBookId,
            user = other,
            screen = testScreen,
            seats = listOf(
                Seat(
                    room = testScreen.room,
                    row = 0,
                    col = 0,
                    seatType = FRONT
                )
            )
        )

        val myPaymentRequest = PaymentRequest(
            bookId = othersBookId,
        )

        every {
            bookRepository.findById(othersBookId)
        } returns Optional.of(othersBookEntity)

        // when, then
        assertThrows(
            IllegalArgumentException::class.java,
        ) { paymentService.pay(loginUser = me, paymentRequest = myPaymentRequest) }
    }

    @Test
    fun `결제 시 결제 엔티티가 생성된다`() {
        // given
        val myBookId = 1L

        val myBookEntity = BookEntity.of(
            id = myBookId,
            user = me,
            screen = testScreen,
            seats = listOf(
                Seat(
                    room = testScreen.room,
                    row = 0,
                    col = 0,
                    seatType = FRONT
                )
            )
        )

        every {
            bookRepository.findById(myBookId)
        } returns Optional.of(myBookEntity)

        val myPaymentRequest = PaymentRequest(
            bookId = myBookId,
        )

        every {
            paymentRepository.save(any())
        } returns PaymentEntity(id = 1L, book = myBookEntity)

        // when
        paymentService.pay(
            loginUser = me,
            paymentRequest = myPaymentRequest
        )

        // then
        verify (atLeast = 1) {
            paymentRepository.save(any())
        }
    }
}