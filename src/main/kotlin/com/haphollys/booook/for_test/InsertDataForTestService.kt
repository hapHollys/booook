package com.haphollys.booook.for_test

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.repository.MovieRepository
import com.haphollys.booook.repository.RoomRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InsertDataForTestService(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository,
    private val screenRepository: ScreenRepository,
    private val userRepository: UserRepository,
    private val priceList: PriceList
) {
    fun testInsert() {
        val movie = MovieEntity(name = "HARRY PORTER", openingDate = LocalDateTime.now())
        movie.play()
        movieRepository.save(movie)

        val user = UserEntity(name = "GALID")
        userRepository.save(user)

        val room = RoomEntity.of(
            numRow = 10,
            numCol = 10
        )
        roomRepository.save(room)

        val screen = ScreenEntity.of(
            movie = movie,
            room = room,
            date = LocalDateTime.now().plusDays(2),
            priceMap = priceList.table
        )
        screenRepository.save(screen)
    }

    fun testScreenInsert() {
        // 대략 한 영화는 많으면 하루에 30번 정도 상영을 함
        val movie = movieRepository.findById(1L).get()
        val room = roomRepository.findById(1L).get()

        for (i in 0..29) {
            screenRepository.save(ScreenEntity.of(
                movie = movie,
                room = room,
                date = LocalDateTime.now().plusDays(2),
                priceMap = priceList.table
            ))
        }
    }

    fun testMovieInsert() {
        for (i in 0 .. 1000) {
            movieRepository.save(MovieEntity(name = "new_movie_${i}", openingDate = LocalDateTime.now(), playing = true))
        }
    }
}
