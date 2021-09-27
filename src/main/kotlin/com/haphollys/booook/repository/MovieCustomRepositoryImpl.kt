package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.movie.QMovieEntity.movieEntity
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory

class MovieCustomRepositoryImpl(
    private val query: JPAQueryFactory
): MovieCustomRepository {
    override fun findCurrentPlayingMovieList(): List<MovieEntity> {
        return query.select(movieEntity)
            .from(movieEntity)
            .where(playingNow())
            .fetch()
    }

    private fun playingNow(): Predicate? {
        return movieEntity.playing.eq(true)
    }
}