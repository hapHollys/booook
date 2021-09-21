package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
class BookDomainService {
    fun book(
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

    fun unBook(
        book: BookEntity
    ) {
        book.unBook()

        book.screen.unBookSeats(
            book.bookedSeats.map {
                it.seatPosition
            }
        )
    }
}