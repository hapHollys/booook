package com.haphollys.booook.domains.screen

import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
        val bookPosition = SeatPosition(x = 0, y = 0)

        // when
        screenEntity.bookSeats(listOf(bookPosition))

        // then
        verify {
            screenEntity.screenRoom.book(bookPosition)
        }
    }

    @Test
    fun `한 좌석 예약 취소`() {
        // given
        val bookPosition = SeatPosition(x = 0, y = 0)
        screenEntity.bookSeats(listOf(bookPosition))

        // when
        screenEntity.unBookSeats(listOf(bookPosition))

        // then
        verify {
            screenEntity.screenRoom.unBook(bookPosition)
        }
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
        bookableSeats.forEach{
            bookedPositions.forEach{ bookedPosition ->
                assertFalse(it.seatPosition == bookedPosition)
            }
        }

        val roomSize = screenEntity.screenRoom.numCol * screenEntity.screenRoom.numRow
        assertEquals(roomSize, bookedPositions.size + bookableSeats.size)
    }
}
