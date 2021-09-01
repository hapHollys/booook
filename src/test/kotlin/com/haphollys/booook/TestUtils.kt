package com.haphollys.booook

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity

fun getTestScreenEntity(
    roomId: Long = 1L,
    movie: MovieEntity = getTestMovie()
): ScreenEntity {
    val room = RoomEntity.of(10, 10, RoomEntity.RoomType.TWO_D)
    room.id = roomId

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