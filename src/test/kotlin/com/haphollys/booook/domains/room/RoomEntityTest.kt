package com.haphollys.booook.domains.room

import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RoomEntityTest {

    @Test
    fun `equals 테스트`() {
        // given
        val room1 = RoomEntity.of(5, 5)
            .apply { id = 1L }
        val room2 = RoomEntity.of(2, 2)
            .apply { id = 1L }

        // when
        val result = room1 == room2

        // then
        assertTrue(result)
    }

    @Test
    fun `hashcode 테스트`() {
        // given
        val numRow = 5
        val numCol = 5
        val roomType = TWO_D

        val room1 = RoomEntity.of(
            numRow = numRow,
            numCol = numCol,
            roomType = roomType
        )
        val room2 = RoomEntity.of(
            numRow = numRow,
            numCol = numCol,
            roomType = roomType
        )

        // when
        val result = room1.hashCode() == room2.hashCode()

        // then
        assertTrue(result)
    }

    @Test
    fun `SeatType 조회`() {
        // given
        val numRow = 5
        val numCol = 5

        val room = RoomEntity.of(
            numRow = numRow,
            numCol = numCol
        )

        // when, then
        assertEquals(FRONT, room.getSeatType(0))

        assertEquals(MIDDLE, room.getSeatType(1))
        assertEquals(MIDDLE, room.getSeatType(2))

        assertEquals(BACK, room.getSeatType(3))
        assertEquals(BACK, room.getSeatType(4))
    }

    @Test
    fun `RoomEntity 생성`() {
        // given
        val numRow = 5
        val numCol = 5

        // when
        val result = RoomEntity.of(
            numRow = numRow,
            numCol = numCol
        )

        // then
        assertEquals(numRow, result.numRow)
        assertEquals(numCol, result.numCol)
        assertEquals(TWO_D, result.roomType)
    }
}
