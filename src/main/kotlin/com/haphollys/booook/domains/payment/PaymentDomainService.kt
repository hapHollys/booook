package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.model.PriceList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentDomainService(
    private val priceList: PriceList,

) {
    fun pay(
        book: BookEntity
    ): PaymentEntity {
        book.pay()
        return PaymentEntity.of(
            book = book,
            priceList = priceList.table
        )
    }

    fun unPay(
        payment: PaymentEntity
    ) {
        payment.unPay()

        payment.book.unBook()

        payment.book.screen.unBookSeats(
            payment.book.bookedSeats.map { it.seatPosition }
        )
    }
}