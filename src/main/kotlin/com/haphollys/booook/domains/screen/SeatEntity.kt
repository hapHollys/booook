package com.haphollys.booook.domains.screen

import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.BOOKED
import com.haphollys.booook.domains.screen.SeatEntity.SeatStatus.FREE
import com.haphollys.booook.model.SeatPosition
import javax.persistence.*

@Entity
@Table(name = "screen_seat")
class SeatEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    var screen: ScreenEntity,
    @Embedded
    var seatPosition: SeatPosition,
    @Enumerated(value = EnumType.STRING)
    var seatType: SeatType,
    @Enumerated(value = EnumType.STRING)
    var status: SeatStatus = FREE,
) {
    enum class SeatStatus {
        BOOKED, FREE
    }

    enum class SeatType {
        FRONT, MIDDLE, BACK
    }

    // TODO: ScreenRoom 비교?
    override fun equals(other: Any?): Boolean {
        other as SeatEntity
        return seatPosition == other.seatPosition
    }

    fun book() {
        verifyBookable()
        this.status = BOOKED
    }

    private fun verifyBookable() {
        if (!bookable())
            throw RuntimeException("예약 불가능한 좌석입니다.")
    }

    fun bookable(): Boolean {
        return this.status == FREE
    }

    fun unBook() {
        verifyUnBookable()
        this.status = FREE
    }

    private fun verifyUnBookable() {
        if (this.status == FREE)
            throw RuntimeException("예매되지 않은 좌석입니다.")
    }
}
