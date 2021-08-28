package com.haphollys.booook.domains.room

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.getTestMovie
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RoomEntityTest {
    @Test
    fun `예약 가능한 좌석만 조회`() {
        // given
        // 특정 좌석이 예약 되어있는 ScreenEntity 생성
        // 좌석 조회
        // 조회된 좌석 수가,

        val screen = ScreenEntity.of(
            movie = getTestMovie(),
            room = RoomEntity.of(
                numRow = 2,
                numCol = 2
            )
        )

        val bookedRow = 0
        val bookedCol = 0
        screen.room.getSeat(
            row = bookedRow,
            col = bookedCol
        ).book()

        // when
        val freeSeats: List<Seat> = screen.room.getFreeSeats()

        // then
        freeSeats.forEach {
            assertFalse(bookedRow == it.row && bookedCol == it.col)
        }
    }
}