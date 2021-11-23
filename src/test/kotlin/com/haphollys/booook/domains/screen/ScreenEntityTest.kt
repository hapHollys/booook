package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.getTestPriceList
import com.haphollys.booook.model.SeatPosition
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ScreenEntityTest {
    lateinit var screenEntity: ScreenEntity
    lateinit var roomEntity: RoomEntity
    lateinit var movieEntity: MovieEntity

    @BeforeEach
    fun setUp() {
        roomEntity = RoomEntity.of(
            numRow = 2,
            numCol = 2
        )
        roomEntity.id = 1L

        movieEntity = mockk(relaxed = true)

        screenEntity = ScreenEntity.of(
            movie = movieEntity,
            room = roomEntity,
            priceMap = getTestPriceList().table
        )
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
        val bookSeatPositions = listOf(SeatPosition(0, 0))
        screenEntity.bookSeats(bookSeatPositions)

        // when
        val bookableSeatPositions = screenEntity.getBookableSeats()
            .map { it.seatPosition }

        // then
        bookSeatPositions.forEach {
            assertFalse(bookableSeatPositions.contains(it))
        }

        val roomSize = screenEntity.screenRoom.numCol * screenEntity.screenRoom.numRow
        assertNotEquals(roomSize, bookableSeatPositions.size)
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
