package com.haphollys.booook.service

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.room.Seat
import com.haphollys.booook.domains.room.Seat.SeatStatus.BOOKED
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.PaymentRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import com.haphollys.booook.service.dto.BookDto
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

        val bookEntity = BookEntity.of(
            user = foundUser,
            screen = foundScreen,
            seats = request.seats
                .map {
                    Seat(
                        room = foundScreen.room,
                        row = it.row,
                        col = it.col,
                        status = BOOKED,
                        seatType = it.type  // TODO 프론트에서 넘겨주는게 맞는지?
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