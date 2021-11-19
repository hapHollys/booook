package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.service.dto.SeatDto
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
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

        testScreen = mockk<ScreenEntity>(relaxed = true)
        testUser = UserEntity(id = 1L, name = "TEST_USER")
    }

    @Test
    fun `좌석 예약`() {
        // given
        val bookSeats = listOf(
            SeatDto(
                row = 0,
                col = 0,
                type = FRONT
            )
        )

        mockkObject(BookEntity.Companion)

        // when
        bookSeatsService.bookSeats(
            user = testUser,
            screen = testScreen,
            bookSeats = bookSeats
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
    fun `내 예약 좌석 취소`() {
        // given
        val userId = 1L
        val myBook = mockk<BookEntity>(relaxed = true)
        every {
            myBook.user.id
        } returns userId

        // when
        bookSeatsService.unBookSeats(
            userId = userId,
            book = myBook,
            screen = testScreen
        )

        // then
        verify {
            myBook.unBook()
        }
        verify {
            testScreen.unBookSeats(any())
        }
    }

    @Test
    fun `남의 예약 좌석 취소 시 예외`() {
        // given
        val myUserId = 1L

        val othersUserId = 2L
        val othersBook = mockk<BookEntity>(relaxed = true)
        every {
            othersBook.user.id
        } returns othersUserId

        // when, then
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) {
            bookSeatsService.unBookSeats(
                userId = myUserId,
                book = othersBook,
                screen = othersBook.screen
            )
        }
    }
}
