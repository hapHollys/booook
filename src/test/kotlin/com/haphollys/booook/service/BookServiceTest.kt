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
import java.util.*

@ExtendWith(MockKExtension::class)
internal class BookServiceTest {
    //    private lateinit var paymentRepository: PaymentRepository
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
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0,
                ),
                seatType = FRONT
            )
        )

        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats
        )

        every {
            bookRepository.save(any())
        } returns bookEntity

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
    fun `예약시 좌석의 상태가 예약됨으로 바뀐다`() {
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
            bookedSeats = bookedSeats
        )

        every {
            bookRepository.save(any())
        } returns bookEntity

        every {
            bookRepository.findById(any())
        } returns Optional.of(bookEntity)

        val userId = 0L
        val screenId = 0L
        val seats = listOf(
            SeatDto(0, 0, FRONT),
            SeatDto(0, 1, FRONT)
        )

        val bookRequest = BookRequest(
            screenId = screenId,
            userId = userId,
            seats = seats
        )

        // when
        val response = bookService.book(bookRequest)

        // then
        testScreen.screenRoom.seats.forEach {
            run {
                var isBookedSeat = false
                seats.forEach { it2: SeatDto ->
                    run {
                        if (it.seatPosition == it2.toSeatPosition()) {
                            isBookedSeat = true
                            assertEquals(BOOKED, it.status)
                        }
                    }
                }
                if (!isBookedSeat) {
                    assertEquals(FREE, it.status)
                }
            }
        }

    }

    @Test
    fun `이미 예약된 좌석은 예약할 수 없다`() {
        // given
        val bookedSeats = listOf(
            BookedSeat(
                SeatPosition(
                    x = 0,
                    y = 0,
                ),
                seatType = FRONT
            )
        )

        val bookEntity = BookEntity.of(
            id = 1L,
            user = testUser,
            screen = testScreen,
            bookedSeats = bookedSeats
        )

        every {
            bookRepository.save(any())
        } returns bookEntity

        every {
            bookRepository.findById(any())
        } returns Optional.of(bookEntity)

        val userId = 0L
        val screenId = 0L
        val seats = listOf(
            SeatDto(0, 0, FRONT),
            SeatDto(0, 1, FRONT)
        )

        val bookRequest = BookRequest(
            screenId = screenId,
            userId = userId,
            seats = seats
        )

        seats.forEach {
            testScreen.screenRoom.getSeat(it.toSeatPosition()).book()
        }

        // then
        assertThrows(
            RuntimeException::class.java
        ) { bookService.book(bookRequest) }

    }

    // TODO 빈 예약시 Excetpion 테스트 추가

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

    // 이미 취소 되었거나, 결제된 경우 익셉션
    @Test
    fun `이미 취소된 예약 취소시 예외`() {
        // given
        val bookId = 1L
        val book = makeBookedSeatBookEntity(bookId)

        val unBookRequest = UnBookRequest(
            bookId = bookId,
            userId = testUser.id!!
        )

        every {
            bookRepository.findById(bookId)
        } returns Optional.of(book)

        bookService.unBook(unBookRequest)

        // when, then
        assertThrows(
            IllegalStateException::class.java
        ) { bookService.unBook(unBookRequest) }
    }

//    @Test
//    fun `이미 결제된 예약 취소시 예외`() {
        // given
//        makeBookedSeatBookEntity()
//
        // when
//
        // then
//    }

    // 유저가 예약한 것이 아닌 경우 익셉션
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
        assertThrows (IllegalArgumentException::class.java) {
            bookService.unBook(unBookRequest)
        }
    }

    private fun makeBookedSeatBookEntity(bookId: Long): BookEntity {
        val bookPosition = SeatPosition(x = 0, y = 0)
        val book = spyk(
            BookEntity.of(
                id = bookId,
                user = testUser,
                screen = testScreen,
                bookedSeats = listOf(
                    BookedSeat(
                        seatPosition = bookPosition,
                        seatType = FRONT
                    ),
                )
            )
        )

        testScreen.screenRoom.getSeat(bookPosition)
            .book()

        return book
    }
}