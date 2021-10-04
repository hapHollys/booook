package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.MovieService
import com.haphollys.booook.service.dto.MovieDto.*
import com.haphollys.booook.service.dto.PagingRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/movies")
class MovieController(
    private val movieService: MovieService
) {
    @GetMapping
    fun getCurrentPlayingMovieList(
        @RequestParam("playingNow") playingNow: Boolean?,
        @ModelAttribute pagingRequest: PagingRequest
    ): Response<List<GetMovieListResponse>> {
        return Response(
            data = movieService.getMovieList(
                request = GetMovieListRequest(
                    playingNow = playingNow,
                    pagingRequest = pagingRequest
                )
            )
        )
    }

    @GetMapping("/{movieId}")
    fun getMovieInfo(
        @PathVariable("movieId") movieId: Long
    ): Response<GetMovieInfoResponse> {
        return Response(
            data = movieService.getMovieInfo(
                request = GetMovieInfoRequest(
                    movieId = movieId
                )
            )
        )
    }
}
