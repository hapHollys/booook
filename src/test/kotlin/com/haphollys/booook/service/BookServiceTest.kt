package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookSeatsService
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.FRONT
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto.*
import com.haphollys.booook.service.dto.SeatDto
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
internal class BookServiceTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var screenRepository: ScreenRepository
    private lateinit var userRepository: UserRepository

    private lateinit var bookService: BookService

    private lateinit var bookSeatsService: BookSeatsService

    lateinit var testUser: UserEntity
    lateinit var testScreen: ScreenEntity

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(id = 1L, name = "TEST_USER")
        testScreen = getTestScreenEntity()

        userRepository = mockk()
        every {
            userRepository.findById(any())
        } returns Optional.of(testUser)

        screenRepository = mockk()
        every {
            screenRepository.findById(any())
        } returns Optional.of(testScreen)

        bookRepository = mockk()

        bookSeatsService = mockk(relaxed = true)

        bookService = spyk(
            BookService(
                bookSeatsService = bookSeatsService,
                bookRepository = bookRepository,
                screenRepository = screenRepository,
                userRepository = userRepository
            )
        )
    }

    @Test
    fun `예약`() {
        // given
        every {
            bookRepository.save(any())
        } returns mockk<BookEntity>(relaxed = true)

        val userId = 0L
        val screenId = 0L
        val bookRequest = BookRequest(
            screenId = screenId,
            userId = userId,
            seats = listOf(
                SeatDto(0, 0, FRONT),
                SeatDto(0, 1, FRONT)
            )
        )

        // when
        bookService.book(bookRequest)

        // then
        verify(atLeast = 1) { bookRepository.save(any()) }
        verify {
            bookSeatsService.bookSeats(
                user = testUser,
                screen = testScreen,
                bookSeats = any()
            )
        }
    }

    @Test
    fun `예약 내역 조회 메소드 호출`() {
        // given
        val userId = 1L
        val request = GetBookedListRequest(1L)

        val foundBook = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen.apply {
                id = 1L
                date = LocalDateTime.now().plusHours(1)
            },
            bookedSeats = mutableListOf(
                BookedSeat(
                    screenId = 1L,
                    seatPosition = SeatPosition(x = 0, y = 0),
                    seatType = FRONT
                )
            ),
        )

        every {
            bookRepository.findByUser_Id(userId)
        } returns listOf(
            foundBook
        )

        // when
        bookService.getBookedList(request)

        // then
        verify(atLeast = 1) {
            bookRepository.findByUser_Id(request.userId)
        }
    }

    @Test
    fun `예약 취소`() {
        // given
        val bookId = 1L
        val book = mockk<BookEntity>(relaxed = true)

        every {
            bookRepository.findById(bookId)
        } returns Optional.of(book)

        val unBookRequest = UnBookRequest(
            bookId = bookId,
            userId = testUser.id!!
        )

        // when
        bookService.unBook(request = unBookRequest)

        // then
        verify(atLeast = 1) {
            bookRepository.findById(bookId)
        }

        verify(atLeast = 1) {
            bookSeatsService.unBookSeats(
                userId = any(),
                book = any(),
                screen = any()
            )
        }
    }
}
