package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.repository.ScreenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class PaymentDomainService(
) {
    fun pay(
        screenRepository: ScreenRepository,
        payerId: Long,
        screenId: Long,
        seatPositions: List<SeatPosition>
    ): PaymentEntity {
        val screen = screenRepository.findById(screenId)
            .orElseThrow{ EntityNotFoundException("없는 상영입니다.") }

        screen.bookSeats(seatPositions)

        return PaymentEntity.of(
            payerId = payerId,
            screen = screen,
            bookedSeats = seatPositions.map {
                val seat = screen.getSeat(seatPosition = it)

                BookedSeat(
                    seatPosition = it,
                    seatType = seat.seatType,
                    seat.price
                )
            }.toMutableList()
        )
    }

    fun unPay(
        userId: Long,
        payment: PaymentEntity,
    ) {
        payment.screen.unBookSeats(payment.bookedSeats.map { it.seatPosition })

        payment.unPay()
    }
}
