package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.room.RoomEntity
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
}