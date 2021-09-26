package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.MovieService
import com.haphollys.booook.service.dto.MovieDto
import com.haphollys.booook.service.dto.MovieDto.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/movies")
class MovieController(
    private val movieService: MovieService
) {
    @GetMapping
    fun getCurrentScreenedMovieList(): Response<List<GetCurrentScreenedMovieResponse>> {
        return Response (
            data = movieService.getCurrentScreenedMovieList()
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