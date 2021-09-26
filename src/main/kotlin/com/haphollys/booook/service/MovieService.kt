package com.haphollys.booook.service

import com.haphollys.booook.repository.MovieRepository
import com.haphollys.booook.service.dto.MovieDto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository
) {
    fun getCurrentScreenedMovieList(

    ): List<GetCurrentScreenedMovieResponse> {
        return movieRepository.findCurrentScreenedMovieResponse()
            .map {
                GetCurrentScreenedMovieResponse(
                    movieId = it.id!!,
                    movieName = it.name
                )
            }
    }

    fun getMovieInfo(
        request: GetMovieInfoRequest
    ): GetMovieInfoResponse {
        val movie = movieRepository.findById(request.movieId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 영화입니다.") }

        return GetMovieInfoResponse(
            movieId = movie.id!!,
            movieName = movie.name
        )
    }
}