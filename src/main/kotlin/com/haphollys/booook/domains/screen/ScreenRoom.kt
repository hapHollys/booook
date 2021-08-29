package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.room.RoomEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.model.SeatPosition
import javax.persistence.*

@Embeddable
class ScreenRoom(
    @Column
    val roomId: Long,
    val numRow: Int,
    val numCol: Int,
    @Enumerated(value = EnumType.STRING)
    val roomType: RoomType,
    @ElementCollection
    // @CollectionTable(name="") TODO: 관계 테이블 이름 명시적으로 지정하기
    val seats: List<Seat>
) {
    fun getSeat(
        seatPosition: SeatPosition
    ): Seat {
        var seat : Seat? = null
        seats.forEach{
            if (it.seatPosition == seatPosition) {
                seat = it
            }
        }
        return seat!!
    }
}
