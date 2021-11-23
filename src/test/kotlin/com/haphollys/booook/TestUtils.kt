package com.haphollys.booook

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.SeatEntity
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
        priceMap = priceList.table
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
                SeatEntity.SeatType.FRONT to BigDecimal(1000),
                SeatEntity.SeatType.MIDDLE to BigDecimal(2000),
                SeatEntity.SeatType.BACK to BigDecimal(1000)
            )
        )
    )
}