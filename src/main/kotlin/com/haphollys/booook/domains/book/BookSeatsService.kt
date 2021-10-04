package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import org.springframework.stereotype.Service

@Service
class BookSeatsService {
    fun bookSeats(
        user: UserEntity,
        screen: ScreenEntity,
        bookedSeats: List<BookedSeat>,
    ): BookEntity {
        val bookEntity = BookEntity.of(
            user = user,
            screen = screen,
            bookedSeats = bookedSeats
        )

        screen.bookSeats(
            seatPositions = bookedSeats.map { it.seatPosition }
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
