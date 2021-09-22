package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookSeatsService
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.model.PriceList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentDomainService(
    private val priceList: PriceList,
    private val bookSeatsService: BookSeatsService
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
        payment: PaymentEntity,
        book: BookEntity,
        screen: ScreenEntity
    ) {
        bookSeatsService.unBookSeats(
            book = book,
            screen = screen
        )

        payment.unPay()
    }
}