package com.haphollys.booook.repository

import com.haphollys.booook.config.TestQueryDslConfig
import com.haphollys.booook.domains.book.BookEntity
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.model.PriceList
import com.haphollys.booook.service.dto.PagingRequest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(TestQueryDslConfig::class)
class PaymentRepositoryTest {
    @Autowired
    lateinit var em: TestEntityManager

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var paymentRepository: PaymentRepository

    lateinit var priceList: PriceList

    val myUserId = 1L
    lateinit var myPayment: PaymentEntity
    lateinit var myBook: BookEntity

    val otherUserId = 2L
    lateinit var otherPayment: PaymentEntity
    lateinit var otherBook: BookEntity

    @BeforeEach
    fun setUp() {
        myBook = mockk(relaxed = true)
        em.persist(myBook)
        otherBook = mockk(relaxed = true)
        em.persist(otherBook)

        priceList = mockk<PriceList>(relaxed = true)
    }

    @Test
    fun `내 결제 내역만 반환`() {
        // given
        myPayment = PaymentEntity.of(
            payerId = myUserId,
            book = myBook,
            priceList = priceList.table
        )

        otherPayment = PaymentEntity.of(
            payerId = otherUserId,
            book = otherBook,
            priceList = priceList.table
        )

        paymentRepository.saveAll(
            listOf(
                myPayment,
                otherPayment
            )
        )

        // when
        val result = paymentRepository.findByUserId(
            userId = myUserId,
            pagingRequest = PagingRequest()
        )

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun `결제 내역 페이징`() {
        // given
        val myId = 1L

        val myPaymentList = listOf(
            PaymentEntity.of(payerId = myId, book = myBook, priceList = priceList.table),
            PaymentEntity.of(payerId = myId, book = myBook, priceList = priceList.table),
            PaymentEntity.of(payerId = myId, book = myBook, priceList = priceList.table)
        )

        paymentRepository.saveAll(myPaymentList)

        // when
        val result = paymentRepository.findByUserId(
            userId = myId,
            pagingRequest = PagingRequest(
                lastId = myPaymentList.last().id!!
            )
        )

        // then
        assertEquals(2, result.size)
    }
}
