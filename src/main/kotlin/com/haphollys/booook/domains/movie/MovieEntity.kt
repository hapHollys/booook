package com.haphollys.booook.domains.movie

import com.haphollys.booook.domains.BaseEntity
import java.lang.IllegalStateException
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "movies")
@Entity
class MovieEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var openingDate: LocalDateTime,
    var playing: Boolean = false
) : BaseEntity() {

    fun play() {
        verifyPlayable()

        playing = true
    }

    fun stop() {
        playing = false
    }

    private fun verifyPlayable() {
        if (openingDate.isAfter(LocalDateTime.now()))
            throw IllegalStateException("아직 개봉일 전입니다.")
    }

    companion object {
        fun of(
            id: Long? = null,
            name: String,
            openingDate: LocalDateTime
        ): MovieEntity {
            return MovieEntity(
                id = id,
                name = name,
                openingDate = openingDate,
            )
        }
    }
}
