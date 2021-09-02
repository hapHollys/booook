package com.haphollys.booook.domains.screen

import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ScreenEntityTest {

    lateinit var screenEntity: ScreenEntity

    @BeforeEach
    fun setUp() {
        screenEntity = getTestScreenEntity()
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
