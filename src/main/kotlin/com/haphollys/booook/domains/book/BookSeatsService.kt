package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.model.SeatPosition
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


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
        book: BookEntity,
        screen: ScreenEntity
    ) {
        book.unBook()

        screen.unBookSeats(
            seatPositions = book.bookedSeats.map { it.seatPosition }
        )
    }
}