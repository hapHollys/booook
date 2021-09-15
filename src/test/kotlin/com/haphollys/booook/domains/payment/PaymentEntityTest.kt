package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.payment.PaymentEntity.Status.CANCEL
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.model.SeatPosition
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaymentEntityTest {

    private lateinit var user: UserEntity
    private lateinit var testScreen: ScreenEntity
    private val testBookId: Long = 1L
    private lateinit var testBook: BookEntity

    @BeforeEach
    fun setUp() {
        user = UserEntity(id = 1L, name = "ME_USER")
        testScreen = getTestScreenEntity()

        testBook = spyk(BookEntity.of(
            id = testBookId,
            user = user,
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
        ))

    }

    @Test
    fun `1명 예약 결제`() {
        // given, when
        val payment = PaymentEntity.of(
            book = testBook,
            priceList = PriceList().table
        )

        // then
        assertEquals(payment.totalAmount, 1000)
    }

    @Test
    fun `결제 취소`() {
        // given
        val payment = PaymentEntity.of(
            book = testBook,
            priceList = PriceList().table
        )

        // when
        payment.unPay()

        // then
        // 결제 엔티티 상태가 취소가 됨
        assertEquals(CANCEL, payment.status)

        // bookEntity 의 unbook 호출
        verify {
            testBook.unBook()
        }
    }

    @Test
    fun `이미 취소된 결제 취소 시 예외`() {
        // given
        val payment = PaymentEntity.of(
            book = testBook,
            priceList = PriceList().table
        )
        payment.unPay()

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            payment.unPay()
        }
    }
}