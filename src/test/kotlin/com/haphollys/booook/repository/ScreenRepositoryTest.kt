package com.haphollys.booook.repository

import com.haphollys.booook.config.TestQueryDslConfig
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.getTestPriceList
import com.haphollys.booook.service.dto.PagingRequest
import org.junit.jupiter.api.Assertions.assertEquals
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

    @Autowired
    lateinit var roomRepository: RoomRepository

    lateinit var testMovie: MovieEntity

    @BeforeEach
    fun setUp() {
        testMovie = MovieEntity(name = "TEST_MOVIE", openingDate = LocalDateTime.now())

        em.persist(testMovie)
        em.flush()
    }

    @Test
    fun `특정 영화 특정 날짜 Screen List 조회`() {
        // given
        val targetDate = LocalDateTime.of(2021, 12, 31, 12, 0)
        val room = RoomEntity.of(numRow = 3, numCol = 3, roomType = TWO_D)
        room.id = 1L

        val beforeScreen = ScreenEntity.of(
            movie = testMovie,
            room = room,
            priceMap = getTestPriceList().table,
            date = targetDate.minusDays(1L)
        )

        val targetScreen = ScreenEntity.of(
            movie = testMovie,
            room = room,
            priceMap = getTestPriceList().table,
            date = targetDate
        )

        val afterScreen = ScreenEntity.of(
            movie = testMovie,
            room = room,
            priceMap = getTestPriceList().table,
            date = targetDate.plusDays(1L)
        )

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
        val room = RoomEntity.of(numRow = 3, numCol = 3, roomType = TWO_D)
        roomRepository.save(room)

        val screenDateTime = LocalDateTime.of(2021, 12, 1, 1, 0)

        val screens = listOf(
            ScreenEntity.of(movie = testMovie, room = room, date = screenDateTime.plusHours(1), priceMap = getTestPriceList().table),
            ScreenEntity.of(movie = testMovie, room = room, date = screenDateTime.plusHours(2), priceMap = getTestPriceList().table),
            ScreenEntity.of(movie = testMovie, room = room, date = screenDateTime.plusHours(3), priceMap = getTestPriceList().table)
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
