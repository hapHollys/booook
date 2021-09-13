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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    lateinit var em: TestEntityManager

    @Autowired
    lateinit var bookRepository: BookRepository

    lateinit var testUser: UserEntity
    lateinit var testMovie: MovieEntity
    lateinit var testScreen: ScreenEntity

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(name = "TEST")
        testMovie = MovieEntity(name = "TEST_MOVIE")
        testScreen = getTestScreenEntity(movie = testMovie)

        em.persist(testUser)
        em.persist(testMovie)
        em.persist(testScreen)

        em.clear()
    }

    @Test
    fun `유저의 예약 내역 반환`() {
        // given
        val userId = 1L
        val bookedList = listOf(
            BookEntity.of(
                user = testUser,
                screen = testScreen,
                bookedSeats = listOf(
                    BookedSeat(
                        seatPosition = SeatPosition(x = 0, y = 0),
                        seatType = Seat.SeatType.BACK
                    )
                )
            ),
            BookEntity.of(
                user = testUser,
                screen = testScreen,
                bookedSeats = listOf(
                    BookedSeat(
                        seatPosition = SeatPosition(x = 0, y = 1),
                        seatType = Seat.SeatType.BACK
                    )
                )
            )
        )

        bookedList.forEach {
            em.persist(it)
        }

        // when
        val foundBookedList = bookRepository.findByUser_Id(userId)

        // then
        assertEquals(bookedList.size, foundBookedList.size)
    }

}