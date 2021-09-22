package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.book.BookSeatsService
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
internal class PaymentDomainServiceTest {
    lateinit var bookSeatsService: BookSeatsService
    lateinit var paymentDomainService: PaymentDomainService

    @BeforeEach
    fun setUp() {
        bookSeatsService = mockk(relaxed = true)
        paymentDomainService = PaymentDomainService(
            priceList = mockk(relaxed = true),
            bookSeatsService = bookSeatsService
        )
    }

    @Test
    fun `결제`() {
        // given
        val bookEntity = mockk<BookEntity>(relaxed = true)

        mockkObject(PaymentEntity.Companion)
        
        // when
        paymentDomainService.pay(
            book = bookEntity
        )

        // then
        verify {
            PaymentEntity.of(
                book = any(),
                priceList = any()
            )
        }

        verify {
            bookEntity.pay()
        }
    }
    
    @Test
    fun `결제 취소`() {
        // given
        val paymentEntity = mockk<PaymentEntity>(relaxed = true)
        val bookEntity = mockk<BookEntity>(relaxed = true)
        
        // when
        paymentDomainService.unPay(
            payment = paymentEntity,
            book = bookEntity,
            screen = bookEntity.screen
        )
        
        // then
        verify {
            paymentEntity.unPay()
        }
        verify {
            bookSeatsService.unBookSeats(
                book = bookEntity,
                screen = bookEntity.screen
            )
        }
    }

}