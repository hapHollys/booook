package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity

interface MovieCustomRepository {
    fun findCurrentPlayingMovieList(): List<MovieEntity>
}
