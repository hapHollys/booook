package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookDomainService
import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto
import com.haphollys.booook.service.dto.BookDto.*
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
    private val bookDomainService: BookDomainService,
    private val bookRepository: BookRepository,
    private val screenRepository: ScreenRepository,
    private val userRepository: UserRepository,
) {

    fun book(
        request: BookDto.BookRequest
    ): BookDto.BookResponse {
        val foundUser = userRepository.findById(request.userId)
            .orElseThrow { RuntimeException("없는 유저 입니다.") }
        val foundScreen = screenRepository.findById(request.screenId)
            .orElseThrow { RuntimeException("없는 상영 입니다.") }

        val bookEntity = bookDomainService.book(
            user = foundUser,
            screen = foundScreen,
            bookedSeats = request.seats
                .map {
                    BookedSeat(
                        seatPosition = it.toSeatPosition(),
                        seatType = it.type
                    )
                }
        )

        return BookDto.BookResponse(
            bookId = bookRepository.save(bookEntity).id!!
        )
    }

    fun getBookedList(
        request: GetBookedListRequest
    ): List<GetBookedResponse> {
        val bookedList = bookRepository.findByUser_Id(request.userId);

        return bookedList.map {
            GetBookedResponse(
                userName = it.user.name,
                bookId = it.id!!,
                screenId = it.screen.id!!,
                movieName = it.screen.movie.name,
                roomId = it.screen.screenRoom.roomId,
                roomType = it.screen.screenRoom.roomType.toString(),
                bookedSeats = it.bookedSeats.map { bookedSeat ->
                    SeatDto(
                        row = bookedSeat.seatPosition.x,
                        col = bookedSeat.seatPosition.y,
                        type = bookedSeat.seatType
                    )
                },
                screenDate = it.screen.date
            )
        }
    }

    fun unBook(request: UnBookRequest) {
        val foundBook = bookRepository.findById(request.bookId)
            .orElseThrow {
                IllegalArgumentException("없는 예약내역 입니다.")
            }

        verifyOwnBook(
            userId = request.userId,
            book = foundBook
        )

        bookDomainService.unBook(foundBook)
    }

    internal fun verifyOwnBook(
        userId: Long,
        book: BookEntity
    ) {
        if (book.user.id!! != userId)
            throw IllegalArgumentException("자신의 예약 내역이 아닙니다.")
    }

}