package com.haphollys.booook.repository

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.service.dto.PagingRequest
import java.time.LocalDateTime

interface ScreenCustomRepository {
    fun findByMovieIdAndDate(
        movieId: Long,
        date: LocalDateTime,
        pagingRequest: PagingRequest
    ): List<ScreenEntity>
}