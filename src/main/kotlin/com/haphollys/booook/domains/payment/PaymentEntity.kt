package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.room.RoomEntity.RoomType
import com.haphollys.booook.domains.screen.Seat.SeatType
import javax.persistence.*

@Table(name = "payments")
@Entity
class PaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne
    var book: BookEntity,
    var totalAmount: Int? = null
) {
    internal fun setTotalAmount(priceList: Map<RoomType, Map<SeatType, Int>>) {
        val roomType = book.screen.screenRoom.roomType

        this.totalAmount = book.bookedSeats
            .sumOf { priceList[roomType]!![it.seatType]!! }
    }

    companion object {
        fun of(book: BookEntity): PaymentEntity {
            return PaymentEntity(
                book = book
            )
        }
    }
}