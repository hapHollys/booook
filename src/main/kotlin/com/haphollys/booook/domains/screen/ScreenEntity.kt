package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.Seat
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "screens")
@Entity
class ScreenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY)
    val movie: MovieEntity,
    @OneToOne(fetch = FetchType.LAZY)
    val room: RoomEntity,
    val date: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun of(
            id: Long? = null,
            movie: MovieEntity,
            room: RoomEntity,
            date: LocalDateTime = LocalDateTime.now()
        ): ScreenEntity {
            return ScreenEntity(
                id = id,
                movie = movie,
                room = room,
                date = date
            )
        }
    }
}