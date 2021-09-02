package com.haphollys.booook.repository

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookedSeat
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.model.SeatPosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ScreenRepositoryTest {

    @Autowired
    lateinit var em: TestEntityManager

    @Autowired
    lateinit var screenRepository: ScreenRepository

    lateinit var testMovie: MovieEntity

    @BeforeEach
    fun setUp() {
        testMovie = MovieEntity(name = "TEST_MOVIE")

        em.persist(testMovie)

        em.clear()
    }

    @Test
    fun `findById 테스트`() {
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

}