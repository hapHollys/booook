package com.haphollys.booook.repository

import com.haphollys.booook.config.TestQueryDslConfig
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.service.dto.PagingRequest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import javax.persistence.EntityManager

@DataJpaTest
@Import(TestQueryDslConfig::class)
class ScreenRepositoryTest {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var screenRepository: ScreenRepository

    lateinit var testMovie: MovieEntity

    @BeforeEach
    fun setUp() {
        testMovie = MovieEntity(name = "TEST_MOVIE", openingDate = LocalDateTime.now())

        em.persist(testMovie)
        em.flush()
    }

    @Test
    fun `findById 테스트`() {
        em.persist(testMovie)
        // given
        val screen1 = getTestScreenEntity(movie = testMovie)
        val screen2 = getTestScreenEntity(movie = testMovie)
        em.persist(screen1)
        em.persist(screen2)
        em.clear()

        // when
        val foundScreen = screenRepository.findById(screen2.id!!).get()

        // then
        assertNotEquals(screen1.id!!, foundScreen.id)
        assertEquals(screen2.id!!, foundScreen.id)
    }

    @Test
    fun `특정 영화 특정 날짜 Screen List 조회`() {
        // given
        val targetDate = LocalDateTime.of(2021, 12, 31, 12, 0)

        val beforeScreen = getTestScreenEntity(movie = testMovie)
        beforeScreen.date = targetDate.minusDays(1L)

        val targetScreen = getTestScreenEntity(movie = testMovie)
        targetScreen.date = targetDate

        val afterScreen = getTestScreenEntity(movie = testMovie)
        afterScreen.date = targetDate.plusDays(1L)

        em.persist(beforeScreen)
        em.persist(targetScreen)
        em.persist(afterScreen)

        // when
        val result = screenRepository.findByMovieIdAndDate(
            movieId = testMovie.id!!,
            date = targetDate,
            pagingRequest = PagingRequest()
        )

        // then
        assertEquals(1, result.size)
        assertSameDate(targetDate, result[0].date)
    }
    
    @Test
    fun `상영 목록 페이징`() {
        // given
        val room = mockk<RoomEntity>(relaxed = true)
        val screenDateTime = LocalDateTime.of(2021, 12, 1,1, 0)

        val screens = listOf(
            ScreenEntity.of(movie = testMovie, room = room, date = screenDateTime.plusHours(1)),
            ScreenEntity.of(movie = testMovie, room = room, date = screenDateTime.plusHours(2)),
            ScreenEntity.of(movie = testMovie, room = room, date= screenDateTime.plusHours(3))
        )

        screenRepository.saveAll(screens)
        
        // when
        val result = screenRepository.findByMovieIdAndDate(
            movieId = testMovie.id!!,
            date = screenDateTime,
            pagingRequest = PagingRequest(
                lastId = screens.last().id!!
            )
        )
        
        // then
        assertEquals(2, result.size)
    }

    private fun assertSameDate(
        compareDate: LocalDateTime,
        comparedDate: LocalDateTime
    ) {
        assertEquals(compareDate.year, comparedDate.year)
        assertEquals(compareDate.monthValue, comparedDate.monthValue)
        assertEquals(compareDate.dayOfMonth, comparedDate.dayOfMonth)
    }
}
