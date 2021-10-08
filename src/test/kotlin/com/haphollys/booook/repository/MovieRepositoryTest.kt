package com.haphollys.booook.repository

import com.haphollys.booook.config.TestQueryDslConfig
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.service.dto.PagingRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime

@DataJpaTest
@Import(TestQueryDslConfig::class)
class MovieRepositoryTest {

    @Autowired
    lateinit var movieRepository: MovieRepository

    lateinit var openedMovieList: List<MovieEntity>

    @BeforeEach
    fun setUp() {
        openedMovieList = listOf(
            MovieEntity.of(
                name = "TEST_MOVIE_1",
                openingDate = LocalDateTime.now().minusDays(1)
            ),
            MovieEntity.of(
                name = "TEST_MOVIE_1",
                openingDate = LocalDateTime.now().minusDays(2)
            )
        )

        movieRepository.saveAll(openedMovieList)
    }

    @Test
    fun `상영중인 영화 리스트 반환`() {
        // given
        // 개봉한 영화중 한 영화만 상영중으로 변경
        openedMovieList[0].play()

        // when
        val result = movieRepository.findAllBy(
            playingNow = true,
            pagingRequest = PagingRequest()
        )

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun `전체 영화 반환`() {
        // when
        val result = movieRepository.findAllBy(
            playingNow = null,
            pagingRequest = PagingRequest()
        )

        // then
        assertEquals(openedMovieList.size, result.size)
    }

    @Test
    fun `영화 목록 페이징`() {
        // given
        val lastId = openedMovieList.last().id

        // when
        val result = movieRepository.findAllBy(
            playingNow = null,
            pagingRequest = PagingRequest(
                lastId = lastId
            )
        )

        // then
        assertEquals(1, result.size)
    }
}
