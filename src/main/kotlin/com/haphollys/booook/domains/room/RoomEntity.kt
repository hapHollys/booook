package com.haphollys.booook.domains.room

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
    val seats: List<Seat>,
    @Enumerated(value = EnumType.STRING)
    val roomType: RoomType
) {

    init {
        setSeats()
    }

    private fun setSeats() {
        if (seats.isEmpty())
            throw NoSeatsException()

        seats.forEach { it.room = this }
    }

    enum class RoomType {
        TWO_D, THREE_D, FOUR_D
    }
}