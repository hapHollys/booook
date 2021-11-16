package com.haphollys.booook.service.dto

import com.haphollys.booook.domains.room.RoomEntity.RoomType
import java.time.LocalDateTime
import javax.validation.constraints.Max

class ScreenDto {
    data class GetScreenRequest(
        var movieId: Long?,
        var date: LocalDateTime,
        var pagingRequest: PagingRequest
    )

    data class GetScreenResponse(
        val screenId: Long,
        val screenDateTime: LocalDateTime,
        val roomSeatNum: Int,
        val remainSeatNum: Int,
        val roomType: RoomType,
    )

    data class GetBookableSeatsRequest(
        val screenId: Long,
    )

    data class GetBookableSeatsResponse(
        val seats: List<SeatDto>
    )

    data class PreemptSeatsRequest(
        var screenId: Long? = null,
        @Max(8)
        val seats: List<SeatDto>
    )

    data class PreemptSeatsResponse(
        val preempted: Boolean
    )
}
