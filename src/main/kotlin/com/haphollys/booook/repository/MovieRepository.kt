package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<MovieEntity, Long>, MovieCustomRepository
