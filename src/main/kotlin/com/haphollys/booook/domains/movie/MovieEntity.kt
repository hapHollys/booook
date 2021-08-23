package com.haphollys.booook.domains.movie

import javax.persistence.*

@Table(name = "movies")
@Entity
class MovieEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val name: String
) {
}