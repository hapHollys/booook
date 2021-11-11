package com.haphollys.booook

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat
import com.haphollys.booook.model.PriceList
import java.math.BigDecimal
import java.time.LocalDateTime

fun getTestScreenEntity(
    room: RoomEntity = RoomEntity.of(10, 10, RoomEntity.RoomType.TWO_D),
    movie: MovieEntity = getTestMovie(),
    priceList: PriceList
): ScreenEntity {
    if (room.id == null) {
        room.id = 1L
    }

    return ScreenEntity.of(
        movie = movie,
        room = room,
        date = LocalDateTime.now().plusHours(10),
        priceTable = priceList.table
    )
}

fun getTestMovie(): MovieEntity {
    return MovieEntity.of(
        name = "TEST_MOVIE",
        openingDate = LocalDateTime.now()
    )
}

fun getTestPriceList(): PriceList {
    return PriceList(
        mapOf(
            RoomEntity.RoomType.TWO_D to mapOf(
                Seat.SeatType.FRONT to BigDecimal(1000),
                Seat.SeatType.MIDDLE to BigDecimal(2000),
                Seat.SeatType.BACK to BigDecimal(1000)
            )
        )
    )
}