package com.haphollys.booook.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haphollys.booook.domains.payment.PaymentEntity
import com.haphollys.booook.service.PaymentService
import com.haphollys.booook.service.dto.PaymentDto.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(controllers = [PaymentController::class])
internal class PaymentControllerTest {
    @Autowired
    lateinit var mvc: MockMvc
    
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var paymentService: PaymentService

    val baseUrl = "/api/v1/payments"

    val myId = 1L

    val myPayment = PaymentEntity.of(
        payerId = myId,
        book = mockk(relaxed = true),
        priceList = mockk(relaxed = true)
    ).apply { id = 1L }

    @Test
    fun `결제 내역 요청`() {
        // given
        every {
            paymentService.getPaymentList(any())
        } returns mockk(relaxed = true)
        
        // when, then
        mvc.get(baseUrl)
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `결제 요청`() {
        // given
        val request = PaymentRequest(
            userId = myId,
            bookId = 1L
        )

        every {
            paymentService.pay(request)
        } returns PaymentResponse(myPayment.id!!)

        // when, then
        mvc.post(baseUrl) {
            content = objectMapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isOk() } }
    }


    @Test
    fun `결제 취소 요청`() {
        // given
        val request = UnPaymentRequest(
            userId = myId,
            paymentId = myPayment.id!!
        )

        every {
            paymentService.unPay(
                unPaymentRequest = request
            )
        } returns UnPaymentResponse(paymentId = myPayment.id!!)

        // when, then
        mvc.put(baseUrl) {
            content = objectMapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

}