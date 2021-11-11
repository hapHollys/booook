package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.payment.PaymentEntity.Status.CANCEL
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class PaymentEntityTest {
    private lateinit var testUser: UserEntity
    private lateinit var testScreen: ScreenEntity
    private lateinit var testBookedSeats: MutableList<BookedSeat>

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(id = 1L, name = "ME_USER")
        testScreen = mockk(relaxed = true)
        testBookedSeats = mockk(relaxed = true)
    }

    @Test
    fun `1명 결제`() {
        // given, when
        val bookedSeat = mockk<BookedSeat>()
        every {
            bookedSeat.price
        } returns BigDecimal(1000)


        val payment = PaymentEntity.of(
            payerId = testUser.id!!,
            screen = testScreen,
            bookedSeats = mutableListOf(bookedSeat)
        )

        // then
        assertEquals(payment.totalAmount, BigDecimal(1000))
    }

    @Test
    fun `결제 취소`() {
        // given
        val payment = PaymentEntity.of(
            payerId = testUser.id!!,
            screen = testScreen,
            bookedSeats = testBookedSeats
        )

        // when
        payment.unPay()

        // then
        assertEquals(CANCEL, payment.status)
        assertNotNull(payment.canceledAt)
    }

    @Test
    fun `이미 취소된 결제 취소 시 예외`() {
        // given
        val payment = PaymentEntity.of(
            payerId = testUser.id!!,
            screen = testScreen,
            bookedSeats = testBookedSeats
        )
        payment.unPay()

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            payment.unPay()
        }
    }
}
