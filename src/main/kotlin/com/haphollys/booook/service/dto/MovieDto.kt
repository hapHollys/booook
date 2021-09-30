package com.haphollys.booook.service.dto

class MovieDto {
    data class GetCurrentPlayingMovieResponse(
        val movieId: Long,
        val movieName: String
    )

    data class GetMovieInfoRequest(
        val movieId: Long
    )

    data class GetMovieInfoResponse(
        val movieId: Long,
        val movieName: String
    )
}
