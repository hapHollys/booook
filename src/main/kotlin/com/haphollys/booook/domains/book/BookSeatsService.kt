package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BookSeatsService {
    fun bookSeats(
        user: UserEntity,
        screen: ScreenEntity,
        bookSeats: List<SeatDto>,
    ): BookEntity {
        val bookEntity = BookEntity.of(
            user = user,
            screen = screen,
            bookedSeats = bookSeats.map {
                BookedSeat(
                    screenId = screen.id!!,
                    seatPosition = it.toSeatPosition(),
                    seatType = it.type
                )
            }.toMutableList(),
        )

        screen.bookSeats(
            seatPositions = bookSeats.map { it.toSeatPosition() }.toMutableList()
        )

        return bookEntity
    }

    fun unBookSeats(
        userId: Long,
        book: BookEntity,
        screen: ScreenEntity
    ) {
        verifyOwnBook(
            userId = userId,
            book = book
        )

        book.unBook()

        screen.unBookSeats(
            seatPositions = book.bookedSeats.map { it.seatPosition }
        )
    }

    internal fun verifyOwnBook(
        userId: Long,
        book: BookEntity
    ) {
        if (book.user.id!! != userId)
            throw IllegalArgumentException("자신의 예약 내역이 아닙니다.")
    }
}
