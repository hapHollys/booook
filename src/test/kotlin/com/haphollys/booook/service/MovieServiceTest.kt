package com.haphollys.booook.service

import com.haphollys.booook.repository.MovieRepository
import com.haphollys.booook.service.dto.MovieDto
import com.haphollys.booook.service.dto.MovieDto.GetMovieInfoRequest
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class MovieServiceTest {

    private lateinit var movieRepository: MovieRepository

    private lateinit var movieService: MovieService

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)

        movieService = spyk(
            MovieService(
                movieRepository = movieRepository
            )
        )
    }

    @Test
    fun `현재 상영중인 영화 조회`() {
        // given
        every {
            movieRepository.findAllBy(playingNow = true, pagingRequest = any())
        } returns listOf(
            mockk(relaxed = true)
        )

        // when
        movieService.getMovieList(
            request = MovieDto.GetMovieListRequest(
                playingNow = true,
                pagingRequest = mockk(relaxed = true)
            )
        )

        // then
        verify {
            movieRepository.findAllBy(
                playingNow = true,
                pagingRequest = any()
            )
        }
    }

    @Test
    fun `한 영화 정보 조회`() {
        // given
        val movieId = 1L
        every {
            movieRepository.findById(movieId)
        } returns Optional.of(mockk(relaxed = true))
        val request = GetMovieInfoRequest(movieId = movieId)

        // when
        movieService.getMovieInfo(request)

        // then
        verify {
            movieRepository.findById(request.movieId)
        }
    }
}
