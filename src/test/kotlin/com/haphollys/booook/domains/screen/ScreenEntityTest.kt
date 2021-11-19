package com.haphollys.booook.domains.screen

import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import io.mockk.mockk
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
        screenEntity.screenRoom = mockk(relaxed = true)
    }

    @Test
    fun `한 좌석 예약`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0))

        // when
        screenEntity.bookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.book(bookPositions)
        }
    }

    @Test
    fun `좌석 예약`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0), SeatPosition(x = 0, y = 1))

        // when
        screenEntity.bookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.book(bookPositions)
        }
    }

    @Test
    fun `한 좌석 예약 취소`() {
        // given
        val bookPositions = listOf(SeatPosition(x = 0, y = 0))
        screenEntity.bookSeats(bookPositions)

        // when
        screenEntity.unBookSeats(bookPositions)

        // then
        verify {
            screenEntity.screenRoom.unBook(bookPositions)
        }
    }

    @Test
    fun `예약 가능한 좌석만 조회`() {
        // given, when
        screenEntity.getBookableSeats()

        // then
        verify(atLeast = 1) {
            screenEntity.screenRoom.getBookableSeats()
        }
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
