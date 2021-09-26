package com.haphollys.booook.service.dto

import java.time.LocalDateTime

class MovieDto {
    data class GetCurrentScreenedMovieResponse(
        val movieId: Long,
        val movieName: String
    )

    data class GetMovieInfoRequest (
        val movieId: Long
    )

    data class GetMovieInfoResponse (
        val movieId: Long,
        val movieName: String
    )
}