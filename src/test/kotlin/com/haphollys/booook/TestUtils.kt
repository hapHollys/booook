package com.haphollys.booook

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity

fun getTestScreenEntity(
    room: RoomEntity = RoomEntity.of(10, 10, RoomEntity.RoomType.TWO_D),
    movie: MovieEntity = getTestMovie()
): ScreenEntity {
    if (room.id == null) {
        room.id = 1L
    }

    return ScreenEntity.of(
        movie = movie,
        room = room,
    )
}

fun getTestMovie(): MovieEntity {
    return MovieEntity.of(
        name = "TEST_MOVIE"
    )
}