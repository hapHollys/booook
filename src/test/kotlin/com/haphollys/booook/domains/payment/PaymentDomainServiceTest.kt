package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.repository.ScreenRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class PaymentDomainServiceTest {
    lateinit var paymentDomainService: PaymentDomainService
    lateinit var screenRepository: ScreenRepository
    lateinit var testScreen: ScreenEntity
    lateinit var testBookedSeats: MutableList<BookedSeat>

    @BeforeEach
    fun setUp() {
        paymentDomainService = PaymentDomainService()
        screenRepository = mockk()

        testScreen = mockk(relaxed = true)
        testBookedSeats = mockk(relaxed = true)

        every {
            screenRepository.findById(any())
        } returns Optional.of(testScreen)
    }

    @Test
    fun `결제`() {
        // given
        val userEntity = mockk<UserEntity>(relaxed = true)
        mockkObject(PaymentEntity.Companion)

        // when
        paymentDomainService.pay(
            payerId = userEntity.id!!,
            screenRepository = screenRepository,
            screenId = testScreen.id!!,
            seatPositions = mockk(relaxed = true)
        )

        // then
        verify {
            PaymentEntity.of(
                payerId = userEntity.id!!,
                screen = testScreen,
                bookedSeats = any()
            )
        }

        verify {
            testScreen.bookSeats(any())
        }
    }

    @Test
    fun `결제 취소`() {
        // given
        val myId = 1L
        val paymentEntity = mockk<PaymentEntity>(relaxed = true)

        // when
        paymentDomainService.unPay(
            userId = myId,
            payment = paymentEntity
        )

        // then
        verify {
            paymentEntity.unPay()
        }
        verify {
            paymentEntity.screen.unBookSeats(any())
        }
    }
}
