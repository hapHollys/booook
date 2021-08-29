package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.screen.Seat
import com.haphollys.booook.domains.screen.Seat.SeatStatus.BOOKED
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val screenRepository: ScreenRepository,
    private val userRepository: UserRepository
) {
    fun book(
        request: BookDto.BookRequest
    ): BookDto.BookResponse {
        val foundUser = userRepository.findById(request.userId)
            .orElseThrow { RuntimeException("없는 유저 입니다.") }
        val foundScreen = screenRepository.findById(request.screenId)
            .orElseThrow { RuntimeException("없는 상영 입니다.") }

        foundScreen.bookSeats(
            request.seats.map{
                it.toSeatPosition()
            }
        )

        val bookEntity = BookEntity.of(
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


//    fun pay() {
//        // 성공 -> 예약 확정
//        // 실패 -> 예약 취소
//    }

}