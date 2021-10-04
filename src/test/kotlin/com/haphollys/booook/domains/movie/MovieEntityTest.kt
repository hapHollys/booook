package com.haphollys.booook.domains.movie

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.IllegalStateException
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class MovieEntityTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `개봉일 전 영화 상영 시작 불가`() {
        // given
        val plus10Day = LocalDateTime.now().plusDays(10)

        val movie = MovieEntity.of(
            name = "TEST_MOVIE",
            openingDate = plus10Day
        )

        // when, then
        assertThrows(
            IllegalStateException::class.java
        ) {
            movie.play()
        }
    }

    @Test
    fun `상영 시작`() {
        // given
        val movie = MovieEntity.of(
            name = "TEST_MOVIE",
            openingDate = LocalDateTime.now()
        )

        // when
        movie.play()

        // then
        assertEquals(true, movie.playing)
    }

    @Test
    fun `상영 끝`() {
        // given
        val movie = MovieEntity.of(
            name = "TEST_MOVIE",
            openingDate = LocalDateTime.now()
        )

        // when
        movie.stop()

        // then
        assertEquals(false, movie.playing)
    }
}
