package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.movie.QMovieEntity.movieEntity
import com.haphollys.booook.service.dto.PagingRequest
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.impl.JPAQueryFactory

class MovieCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : MovieCustomRepository {
    override fun findAllBy(
        playingNow: Boolean?,
        pagingRequest: PagingRequest
    ): List<MovieEntity> {
        return query.select(movieEntity)
            .from(movieEntity)
            .where(playingNow(playingNow), cursorPosition(pagingRequest.lastId))
            .limit(pagingRequest.size)
            .fetch()
    }

    private fun playingNow(
        playingNow: Boolean?
    ): Predicate? {
        if (playingNow == null) return null

        return movieEntity.playing.eq(true)
    }

    private fun cursorPosition(lastId: Long?): Predicate? {
        if (lastId == null) return null

        return movieEntity.id.lt(lastId)
    }
}
