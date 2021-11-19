package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.FREE
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.FRONT
import com.haphollys.booook.model.SeatPosition
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class SeatEntityTest {

    lateinit var seat: SeatEntity

    @BeforeEach
    fun setUp() {
        seat = SeatEntity(
            screen = mockk(),
            seatPosition = SeatPosition(
                x = 0,
                y = 0,
            ),
            seatType = FRONT
        )
    }

    @Test
    fun `좌석 예약`() {
        // when
        seat.book()

        // then
        assertEquals(BOOKED, seat.status)
    }

    @Test
    fun `좌석 예약 취소`() {
        // given
        seat.book()

        // when
        seat.unBook()

        // then
        assertEquals(FREE, seat.status)
    }

    @Test
    fun `이미 예약된 좌석은 예약할 수 없다`() {
        // given
        seat.book()

        // when, then
        assertThrows(RuntimeException::class.java) {
            seat.book()
        }
    }

    @Test
    fun `예약되지 않은 좌석 예약 취소 시 예외`() {
        // when, then
        assertThrows(RuntimeException::class.java) {
            seat.unBook()
        }
    }

    @Test
    fun `예약 가능 상태 확인`() {
        // when, then
        assertEquals(true, seat.bookable())
    }

    @Test
    fun `예약 불가능 상태 확인`() {
        // given
        seat.book()

        // when, then
        assertEquals(false, seat.bookable())
    }

//    @Test
//    fun `예약 가능한 좌석만 조회`() {
//        // given
//        val bookedPositions = listOf<SeatPosition>(
//            SeatPosition(0, 0),
//            SeatPosition(0, 1),
//            SeatPosition(1, 0),
//            SeatPosition(1, 1),
//        )
//
//        screenEntity.bookSeats(
//            bookedPositions
//        )
//
//        // when
//        val bookableSeats = screenEntity.getBookableSeats()
//
//        // then
//        bookableSeats.forEach {
//            bookedPositions.forEach { bookedPosition ->
//                Assertions.assertFalse(it.seatPosition == bookedPosition)
//            }
//        }
//
//        val roomSize = screenEntity.screenRoom.numCol * screenEntity.screenRoom.numRow
//        assertEquals(roomSize, bookedPositions.size + bookableSeats.size)
//    }
}
