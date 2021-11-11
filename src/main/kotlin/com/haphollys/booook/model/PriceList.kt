package com.haphollys.booook.model

import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.Seat
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PriceList(
    val table: Map<RoomEntity.RoomType, Map<Seat.SeatType, BigDecimal>> = mapOf(
        TWO_D to mapOf(
            FRONT to BigDecimal(1000),
            MIDDLE to BigDecimal(2000),
            BACK to BigDecimal(3000)
        ),
    )
)
