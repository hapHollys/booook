package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity

class MovieCustomRepositoryImpl(

): MovieCustomRepository {
    override fun findCurrentScreenedMovieResponse(): List<MovieEntity> {
        // redis를 이용해서 리턴
        return listOf()
    }
}