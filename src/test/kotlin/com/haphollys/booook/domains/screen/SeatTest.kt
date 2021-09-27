package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.screen.Seat.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.Seat.SeatStatus.FREE
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.model.SeatPosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class SeatTest {
    @Test
    fun `좌석 예약`() {
        // given
        val seat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT
        )

        // when
        seat.book()

        // then
        assertEquals(BOOKED, seat.status)
    }

    @Test
    fun `좌석 예약 취소`() {
        // given
        val seat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT
        )
        seat.book()

        // when
        seat.unBook()

        // then
        assertEquals(FREE, seat.status)
    }

    @Test
    fun `이미 예약된 좌석은 예약할 수 없다`() {
        // given
        val bookedSeat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            status = BOOKED
        )

        assertThrows(RuntimeException::class.java) {
            bookedSeat.book()
        }
    }

    @Test
    fun `예약되지 않은 좌석 예약 취소시 예외`() {
        // given
        val freeSeat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0
            ),
            seatType = FRONT,
            status = FREE
        )

        // when, then
        assertThrows(RuntimeException::class.java) {
            freeSeat.unBook()
        }
    }

    @Test
    fun `예약 가능 상태 확인`() {
        // given
        val seat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT
        )

        // when, then
        assertEquals(true, seat.bookable())
    }

    @Test
    fun `예약 불가능 상태 확인`() {
        // given
        val seat = Seat(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT
        )
        seat.book()

        // when, then
        assertEquals(false, seat.bookable())
    }
}