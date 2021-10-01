package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.service.dto.PagingRequest

interface MovieCustomRepository {
    fun findAllBy(
        playingNow: Boolean?,
        pagingRequest: PagingRequest
    ): List<MovieEntity>
}
