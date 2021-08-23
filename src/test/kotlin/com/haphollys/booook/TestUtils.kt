package com.haphollys.booook

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity

fun getTestScreenEntity(): ScreenEntity {
    return ScreenEntity(
        movie = MovieEntity(
            name = "TEST_MOVIE"
        ),
        room = RoomEntity.of(10, 10, RoomEntity.RoomType.TWO_D),
    )
}