package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.screen.Seat.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.Seat.SeatStatus.FREE
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto
import com.haphollys.booook.service.dto.BookDto.*
import com.haphollys.booook.service.dto.SeatDto
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.*

@ExtendWith(MockKExtension::class)
internal class BookServiceTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var screenRepository: ScreenRepository
    private lateinit var userRepository: UserRepository
    private lateinit var bookService: BookService

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

        bookService = BookService(
            bookRepository = bookRepository,
            screenRepository = screenRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `예약 시 예약 엔티티가 생성된다`() {
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
    }

    @Test
    fun `예약 내역 조회 메소드 호출`() {
        // given
        val userId = 1L
        val request = GetBookedListRequest(1L)

        every {
            bookRepository.findByUser_Id(userId)
        } returns listOf()

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
        val book = makeBookedSeatBookEntity(bookId)

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
            book.unBook()
        }
    }

    @Test
    fun `유저의 예약이 아닌 경우 예약 취소시 예외`() {
        // given
        val bookId = 1L
        val myBook = makeBookedSeatBookEntity(bookId)

        val otherUserId = 2L
        val unBookRequest = UnBookRequest(
            bookId = bookId,
            userId = otherUserId
        )

        every {
            bookRepository.findById(bookId)
        } returns Optional.of(myBook)

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            bookService.unBook(unBookRequest)
        }
    }

    private fun makeBookedSeatBookEntity(
        bookId: Long = 1L,
        bookedSeats: List<BookedSeat> = listOf(
            BookedSeat(
                seatPosition = SeatPosition(x = 0, y = 0),
                seatType = FRONT
            ),
        )
    ): BookEntity {
        val book = spyk(
            BookEntity.of(
                id = bookId,
                user = testUser,
                screen = testScreen,
                bookedSeats = bookedSeats
            )
        )

        return book
    }
}