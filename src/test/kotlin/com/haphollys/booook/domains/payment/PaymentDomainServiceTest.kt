package com.haphollys.booook.domains.payment

import com.haphollys.booook.domains.book.BookEntity
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

    lateinit var paymentDomainService: PaymentDomainService

    @BeforeEach
    fun setUp() {
        paymentDomainService = PaymentDomainService(
            priceList = mockk(relaxed = true)
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
        
        // when
        paymentDomainService.unPay(payment = paymentEntity)
        
        // then
        verify {
            paymentEntity.unPay()
        }

        verify {
            paymentEntity.book.unBook()
        }

        verify {
            paymentEntity.book.screen.unBookSeats(any())
        }
    }

}