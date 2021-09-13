package com.haphollys.booook.for_test

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.repository.BookRepository
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class InsertDataForTestService(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository,
    private val bookRepository: BookRepository,
    private val screenRepository: ScreenRepository,
    private val userRepository: UserRepository,
) {
    fun testInsert() {
        val movie = MovieEntity(name = "HARRY PORTER")
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
        )
        screenRepository.save(screen)
    }
}