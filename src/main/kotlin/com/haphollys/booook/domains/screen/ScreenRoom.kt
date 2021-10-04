package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.model.SeatPosition
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class ScreenRoom(
    var roomId: Long,
    var numRow: Int,
    var numCol: Int,
    @Enumerated(value = EnumType.STRING)
    var roomType: RoomType,
    @ElementCollection
    // @CollectionTable(name="") TODO: 관계 테이블 이름 명시적으로 지정하기
    var seats: List<Seat>
) {
    fun book(bookSeatPosition: SeatPosition) {
        getSeat(bookSeatPosition).book()
    }

    fun unBook(bookSeatPosition: SeatPosition) {
        getSeat(bookSeatPosition).unBook()
    }

    private fun getSeat(
        seatPosition: SeatPosition
    ): Seat {
        var seat: Seat? = null
        seats.forEach {
            if (it.seatPosition == seatPosition) {
                seat = it
            }
        }
        return seat!!
    }
}
