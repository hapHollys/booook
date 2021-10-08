package com.haphollys.booook.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.MovieService
import com.haphollys.booook.service.dto.MovieDto.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@WebMvcTest(controllers = [MovieController::class])
internal class MovieControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var movieService: MovieService

    val baseUrl = "/api/v1/movies"

    val currentPlayingMovie = MovieEntity.of(
        name = "HARRY PORTER",
        openingDate = LocalDateTime.of(2020, 1, 1, 1, 1)
    ).apply { id = 1L }

    @Test
    fun `현재 상영 영화 목록 요청`() {
        val playingNow = true
        val lastId: Long? = null
        val size: Int? = null

        val getMovieListResponse = GetMovieListResponse(
            movieId = currentPlayingMovie.id!!,
            movieName = currentPlayingMovie.name
        )
        every {
            movieService.getMovieList(any())
        } returns listOf(
            getMovieListResponse
        )

        mvc.get(baseUrl) {
            param("playingNow", playingNow.toString())
            lastId?.let { param("lastId", lastId.toString()) }
            size?.let { param("size", size.toString()) }
        }.andExpect {
            status { isOk() }
            content { json(objectMapper.writeValueAsString(Response(listOf(getMovieListResponse)))) }
        }
    }

    @Test
    fun `영화 정보 요청`() {
        // given
        val request = GetMovieInfoRequest(
            movieId = currentPlayingMovie.id!!,
        )

        every {
            movieService.getMovieInfo(request)
        } returns GetMovieInfoResponse(
            movieId = currentPlayingMovie.id!!,
            movieName = currentPlayingMovie.name
        )

        // when, then
        mvc.get(baseUrl + "/${currentPlayingMovie.id}")
            .andExpect {
                status { isOk() }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Response(
                                GetMovieInfoResponse(
                                    movieId = currentPlayingMovie.id!!,
                                    movieName = currentPlayingMovie.name
                                )
                            )
                        )
                    )
                }
            }
    }
}
