package com.haphollys.booook.service.dto

class MovieDto {
    data class GetMovieListRequest(
        val playingNow: Boolean?,
        val pagingRequest: PagingRequest
    )

    data class GetMovieListResponse(
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
