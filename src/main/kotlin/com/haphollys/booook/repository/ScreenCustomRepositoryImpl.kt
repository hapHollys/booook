package com.haphollys.booook.repository

import com.haphollys.booook.domains.screen.QScreenEntity.screenEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.util.DateUtil
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

class ScreenCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : ScreenCustomRepository {

    override fun findByMovieIdAndDate(
        movieId: Long,
        date: LocalDateTime,
        pagingRequest: PagingRequest
    ): List<ScreenEntity> {
        return query.select(screenEntity)
            .from(screenEntity)
            .where(movieIdEq(movieId), dateEq(date), cursorPosition(pagingRequest.lastId))
            .limit(pagingRequest.size)
            .fetch()
    }

    private fun movieIdEq(movieId: Long): Predicate? {
        return screenEntity.movie.id.eq(movieId)
    }

    private fun dateEq(date: LocalDateTime): BooleanExpression? {
        return screenEntity.date.between(DateUtil.getStartTimeOfDay(date), DateUtil.getEndTimeOfDay(date))
    }

    private fun cursorPosition(cursor: Long?): Predicate? {
        if (cursor == null) return null

        return screenEntity.id.lt(cursor)
    }
}