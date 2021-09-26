package com.haphollys.booook.domains.movie

import com.haphollys.booook.domains.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "movies")
@Entity
class MovieEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var openingDate: LocalDateTime
): BaseEntity() {

    companion object {
        fun of(
            id: Long? = null,
            name: String,
            openingDate: LocalDateTime
        ): MovieEntity {
            return MovieEntity(
                id = id,
                name = name,
                openingDate = openingDate
            )
        }
    }
}