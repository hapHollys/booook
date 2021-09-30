package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.book.BookEntity.BookStatus.CANCEL
import com.haphollys.booook.domains.book.BookEntity.BookStatus.PAID
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class BookEntityTest {

    lateinit var testUser: UserEntity
    lateinit var testScreen: ScreenEntity

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(id = 1L, name = "TEST_USER")
        testScreen = spyk(getTestScreenEntity())
    }

    @Test
    fun `예약 취소`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0
                ),
                seatType = FRONT
            )
        )
        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats,
        )

        // when
        bookEntity.unBook()

        // then
        assertEquals(CANCEL, bookEntity.status)
    }

    @Test
    fun `이미 취소된 예약 취소 시 예외`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0
                ),
                seatType = FRONT
            )
        )
        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats,
        )
        bookEntity.unBook()

        // when, then
        assertThrows(
            IllegalStateException::class.java
        ) {
            bookEntity.unBook()
        }
    }

    @Test
    fun `이미 결제된 예약 취소 시 예외`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0
                ),
                seatType = FRONT
            )
        )
        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats,
        )
        bookEntity.pay()

        // when, then

        assertThrows(
            IllegalStateException::class.java
        ) { bookEntity.unBook() }
    }

    @Test
    fun `결제`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0
                ),
                seatType = FRONT
            )
        )
        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats,
        )

        // when
        bookEntity.pay()

        // then
        assertEquals(PAID, bookEntity.status)
    }

    @Test
    fun `예약되지 않은 좌석 결제 시 예외`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0
                ),
                seatType = FRONT
            )
        )
        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats,
        )

        bookEntity.unBook()

        // when,  then
        assertThrows(IllegalArgumentException::class.java) {
            bookEntity.pay()
        }
    }
}
