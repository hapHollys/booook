package com.haphollys.booook.domains.room

import com.haphollys.booook.domains.BaseEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.screen.Seat.SeatType
import com.haphollys.booook.domains.screen.Seat.SeatType.*
import javax.persistence.*

@Table(name = "rooms")
@Entity
class RoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var numRow: Int,
    var numCol: Int,
    @Enumerated(value = EnumType.STRING)
    var roomType: RoomType = TWO_D
): BaseEntity() {
    enum class RoomType {
        TWO_D, THREE_D, FOUR_D
    }

    // TODO casting
    override fun equals(other: Any?): Boolean {
        return id == (other as RoomEntity).id
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + numRow
        result = 31 * result + numCol
        result = 31 * result + roomType.hashCode()
        return result
    }

    fun getSeatType(
        row: Int
    ): SeatType {
        return when {
            row < numRow / 3 -> {
                FRONT
            }
            row < (numRow * 2 / 3) -> {
                MIDDLE
            }
            else -> {
                BACK
            }
        }
    }

    companion object {
        fun of(
            numRow: Int,
            numCol: Int,
            roomType: RoomType = TWO_D
        ): RoomEntity {
            val roomEntity = RoomEntity(
                numRow = numRow,
                numCol = numCol,
                roomType = roomType
            )

            return roomEntity
        }

    }
}
