package com.haphollys.booook.domains.screen

import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ScreenEntityTest {
    lateinit var screenEntity: ScreenEntity

    @BeforeEach
    fun setUp() {
        screenEntity = getTestScreenEntity()
        screenEntity.screenRoom = spyk(screenEntity.screenRoom)
    }

    @Test
    fun `한 좌석 예약`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0))
        val numRemainSeatsBeforeBook = screenEntity.getNumRemainSeats()

        // when
        screenEntity.bookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.book(bookPositions)
        }
        assertTrue(screenEntity.getNumRemainSeats() == numRemainSeatsBeforeBook - bookPositions.size)
    }

    @Test
    fun `좌석 예약`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0), SeatPosition(x = 0, y = 1))
        val numRemainSeatsBeforeBook = screenEntity.getNumRemainSeats()

        // when
        screenEntity.bookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.book(bookPositions)
        }
        assertTrue(screenEntity.getNumRemainSeats() == numRemainSeatsBeforeBook - bookPositions.size)
    }

    @Test
    fun `한 좌석 예약 취소`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0))
        screenEntity.bookSeats(bookPositions)
        val numRemainSeatsBeforeBook = screenEntity.getNumRemainSeats()

        // when
        screenEntity.unBookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.unBook(bookPositions)
        }
        assertTrue(screenEntity.getNumRemainSeats() == numRemainSeatsBeforeBook + bookPositions.size)
    }

    @Test
    fun `예약 가능한 좌석만 조회`() {
        // given
        val bookedPositions = listOf<SeatPosition>(
            SeatPosition(0, 0),
            SeatPosition(0, 1),
            SeatPosition(1, 0),
            SeatPosition(1, 1),
        )

        screenEntity.bookSeats(
            bookedPositions
        )

        // when
        val bookableSeats = screenEntity.getBookableSeats()

        // then
        bookableSeats.forEach {
            bookedPositions.forEach { bookedPosition ->
                assertFalse(it.seatPosition == bookedPosition)
            }
        }

        val roomSize = screenEntity.screenRoom.numCol * screenEntity.screenRoom.numRow
        assertEquals(roomSize, bookedPositions.size + bookableSeats.size)
    }

    @Test
    fun `좌석 수 조회`() {
        // when
        val result = screenEntity.getNumSeats()

        // then
        val seatNum = screenEntity.screenRoom.numRow * screenEntity.screenRoom.numCol
        assertEquals(seatNum, result)
    }
}
