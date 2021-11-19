package com.haphollys.booook.model

import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.SeatEntity
import com.haphollys.booook.domains.screen.SeatEntity.SeatType.*
import org.springframework.stereotype.Component

@Component
class PriceList(
    val table: Map<RoomEntity.RoomType, Map<SeatEntity.SeatType, Int>> = mapOf(
        TWO_D to mapOf(
            FRONT to 1000,
            MIDDLE to 2000,
            BACK to 3000
        ),
    )
)
