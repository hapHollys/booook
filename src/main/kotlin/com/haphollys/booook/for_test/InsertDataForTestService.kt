package com.haphollys.booook.for_test

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class InsertDataForTestService(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository,
    private val bookRepository: BookRepository,
    private val screenRepository: ScreenRepository,
    private val userRepository: UserRepository,
) {
    var movie: MovieEntity? = null
    var room: RoomEntity? = null

    fun testInsert() {
        movie = MovieEntity(name = "HARRY PORTER", openingDate = LocalDateTime.now())
        movie!!.play()
        movieRepository.save(movie!!)

        val user = UserEntity(name = "GALID")
        userRepository.save(user)

        room = RoomEntity.of(
            numRow = 10,
            numCol = 10
        )
        roomRepository.save(room!!)

        val screen = ScreenEntity.of(
            movie = movie!!,
            room = room!!,
        )
        screenRepository.save(screen)
    }

    fun testScreenInsert() {
        // 대략 한 영화는 많으면 하루에 30번 정도 상영을 함
        for (i in 0..29) {
            screenRepository.save(ScreenEntity.of(
                movie = movie!!,
                room = room!!,
            ))
        }
    }
}
