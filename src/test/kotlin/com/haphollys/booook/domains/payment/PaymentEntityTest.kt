package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.domains.room.RoomEntity.RoomType.*
import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.model.SeatPosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaymentEntityTest {

    var priceList: Map<RoomType, Map<SeatType, Int>> = mapOf()

    @BeforeEach
    fun setUp() {
        priceList = mapOf(
            TWO_D to mapOf(
                FRONT to 1000,
                MIDDLE to 2000,
                BACK to 3000
            ),
        )
    }

    @Test
    fun `1명 예약 테스트`() {
        // given
        val movie = MovieEntity(
            name = "Harry Porter"
        )

        val room = RoomEntity.of(
            numRow = 10,
            numCol = 20,
            roomType = TWO_D,
        )
        room.id = 1L

        val screen = ScreenEntity.of(
            movie = movie,
            room = room
        )

        val user = UserEntity(
            name = "user"
        )

        val bookedSeats : List<BookedSeat> = listOf(
            BookedSeat(
                SeatPosition(
                    row = 0,
                    col = 0,
                ),
                seatType = FRONT
            )
        )

        val book = BookEntity.of(
            id = 1L,
            user,
            screen = screen,
            bookedSeats = bookedSeats
        )

        // when
        val payment = PaymentEntity(
            book = book
        )
        payment.setTotalAmount(priceList)

        // then
        assertEquals(payment.totalAmount, 1000)
    }
}