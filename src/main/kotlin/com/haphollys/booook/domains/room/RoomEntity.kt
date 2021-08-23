package com.haphollys.booook.domains.room

import com.haphollys.booook.domains.room.RoomEntity.RoomType.TWO_D
import com.haphollys.booook.domains.room.Seat.SeatType
import com.haphollys.booook.domains.room.Seat.SeatType.*
import javax.persistence.*

@Table(name = "rooms")
@Entity
class RoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val numRow: Int,
    val numCol: Int,
    @ElementCollection
    val seats: MutableList<Seat>,
    @Enumerated(value = EnumType.STRING)
    val roomType: RoomType = TWO_D
) {
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
        result = 31 * result + seats.hashCode()
        result = 31 * result + roomType.hashCode()
        return result
    }

    companion object {
        fun of(
            numRow: Int,
            numCol: Int,
            roomType: RoomType
        ): RoomEntity {
            val seats: MutableList<Seat> = ArrayList()

            val roomEntity = RoomEntity(
                numRow = numRow,
                numCol = numCol,
                seats = seats,
                roomType = roomType
            )

            for (row in 0 .. numRow) {
                for (col in 0 .. numCol) {
                    seats.add(Seat(
                        room = roomEntity,
                        row = row,
                        col = col,
                        seatType = getSeatType(numRow, row)
                    ))
                }
            }

            return roomEntity
        }

        private fun getSeatType(
            numRow: Int,
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
    }
}