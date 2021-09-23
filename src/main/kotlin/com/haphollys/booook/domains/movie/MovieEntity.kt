package com.haphollys.booook.domains.movie

import com.haphollys.booook.domains.BaseEntity
import javax.persistence.*

@Table(name = "movies")
@Entity
class MovieEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String
): BaseEntity() {

    companion object {
        fun of(
            id: Long? = null,
            name: String
        ): MovieEntity {
            return MovieEntity(
                id = id,
                name = name
            )
        }
    }
}