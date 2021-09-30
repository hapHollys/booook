package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.payment.PaymentEntity.Status.CANCEL
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.model.SeatPosition
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaymentEntityTest {

    private lateinit var testUser: UserEntity
    private lateinit var testScreen: ScreenEntity
    private lateinit var testBook: BookEntity

    private lateinit var priceList: PriceList

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(id = 1L, name = "ME_USER")
        testScreen = getTestScreenEntity()

        testBook = mockk<BookEntity>(relaxed = true)

        priceList = PriceList(
            mapOf(
                TWO_D to mapOf(
                    FRONT to 1000,
                    MIDDLE to 2000,
                    BACK to 1000
                ),
            )
        )
    }

    @Test
    fun `1명 예약 결제`() {
        // given, when
        every {
            testBook.bookedSeats
        } returns listOf(
            BookedSeat(
                seatPosition = SeatPosition(x = 0, y = 0),
                seatType = FRONT
            )
        )

        every {
            testBook.screen.screenRoom.roomType
        } returns TWO_D

        val payment = PaymentEntity.of(
            payerId = testUser.id!!,
            book = testBook,
            priceList = priceList.table
        )

        // then
        assertEquals(payment.totalAmount, 1000)
    }

    @Test
    fun `결제 취소`() {
        // given
        val payment = PaymentEntity.of(
            payerId = testUser.id!!,
            book = testBook,
            priceList = PriceList().table
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
