package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.FREE
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.FRONT
import com.haphollys.booook.model.SeatPosition
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class SeatEntityTest {
    @Test
    fun `좌석 예약`() {
        // given
        val seat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            price = mockk(),
            screen = mockk()
        )

        // when
        seat.book()

        // then
        assertEquals(BOOKED, seat.status)
    }

    @Test
    fun `좌석 예약 취소`() {
        // given
        val seat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            price = mockk(),
            screen = mockk()
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
        val bookedSeat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            status = BOOKED,
            price = mockk(),
            screen = mockk()
        )

        assertThrows(RuntimeException::class.java) {
            bookedSeat.book()
        }
    }

    @Test
    fun `예약되지 않은 좌석 예약 취소시 예외`() {
        // given
        val freeSeat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0
            ),
            seatType = FRONT,
            status = FREE,
            price = mockk(),
            screen = mockk()
        )

        // when, then
        assertThrows(RuntimeException::class.java) {
            freeSeat.unBook()
        }
    }

    @Test
    fun `예약 가능 상태 확인`() {
        // given
        val seat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            price = mockk(),
            screen = mockk()
        )

        // when, then
        assertEquals(true, seat.bookable())
    }

    @Test
    fun `예약 불가능 상태 확인`() {
        // given
        val seat = SeatEntity(
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT,
            price = mockk(),
            screen = mockk()
        )
        seat.book()

        // when, then
        assertEquals(false, seat.bookable())
    }
}
