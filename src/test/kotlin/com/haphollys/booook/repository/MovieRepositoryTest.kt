package com.haphollys.booook.repository

import com.haphollys.booook.config.TestQueryDslConfig
import com.haphollys.booook.domains.movie.MovieEntity
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

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `상영중인 영화 리스트 반환`() {
        // given
        val openedMovieList = listOf(
            MovieEntity.of(
                name = "TEST_MOVIE_1",
                openingDate = LocalDateTime.now().minusDays(1)
            ),
            MovieEntity.of(
                name = "TEST_MOVIE_1",
                openingDate = LocalDateTime.now().minusDays(2)
            )
        )

        openedMovieList.forEach { it.play() }

        movieRepository.saveAll(
            openedMovieList
        )

        // when
        val result = movieRepository.findCurrentPlayingMovieList()

        // then
        assertEquals(openedMovieList.size, result.size)
    }
}
