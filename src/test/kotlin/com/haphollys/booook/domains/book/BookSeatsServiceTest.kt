package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class BookSeatsServiceTest {

    lateinit var bookSeatsService: BookSeatsService

    lateinit var testUser: UserEntity
    lateinit var testScreen: ScreenEntity

    @BeforeEach
    fun setUp() {
        bookSeatsService = BookSeatsService()

        testScreen = spyk(getTestScreenEntity())
        testUser = UserEntity(id = 1L, name = "TEST_USER")
    }

    @Test
    fun `좌석 예약`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                seatPosition = SeatPosition(x = 0, y = 0),
                seatType = FRONT
            )
        )

        mockkObject(BookEntity.Companion)

        // when
        bookSeatsService.bookSeats(
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats
        )

        // then
        verify {
            BookEntity.of(
                user = any(),
                screen = any(),
                bookedSeats = any()
            )
        }

        verify {
            testScreen.bookSeats(any())
        }
    }

    @Test
    fun `좌석 예약 취소`() {
        // given
        val myBook = mockk<BookEntity>(relaxed = true)

        // when
        bookSeatsService.unBookSeats(
            book = myBook,
        )

        // then
        verify {
            myBook.unBook()
        }

        verify {
            myBook.screen.unBookSeats(any())
        }
    }
}