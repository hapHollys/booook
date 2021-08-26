package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.room.Seat
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.PaymentDto
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PaymentServiceTest {

    lateinit var testUser: UserEntity
    lateinit var testScreen: ScreenEntity
    lateinit var paymentService: PaymentService
    private lateinit var userRepository: UserRepository
    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(id = 1L, name = "TEST_USER")
        testScreen = getTestScreenEntity()

        userRepository = mockk()
        bookRepository = mockk()
        paymentService = PaymentService(
                userRepository,
                bookRepository
        )
    }

    @Test
    fun `예약된 좌석만 결제할 수 있다`() {
        every {
            bookRepository.findById(any())
        } returns Optional.empty()

        val notExistsBookId = 1234L

        val paymentRequest = PaymentDto.PaymentRequest(
                bookId = notExistsBookId,
                userId = testUser.id!!
        )

        assertThrows(
                IllegalArgumentException::class.java,
                { paymentService.pay(paymentRequest) }
        )
    }

    @Test
    fun `본인이 예약한 좌석만 결제할 수 있다`() {

    }

    @Test
    fun `결제 시 결제 엔티티가 생성된다`() {

    }
}