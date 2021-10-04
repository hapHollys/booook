package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.PaymentService
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.PaymentDto
import com.haphollys.booook.service.dto.PaymentDto.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @GetMapping
    fun getPayments(
        @ModelAttribute pagingRequest: PagingRequest
    ): Response<List<PaymentDto.GetPaymentResponse>> {
        println("paging ${pagingRequest.size} , ${pagingRequest.lastId}")

        val request = GetPaymentRequest(
            userId = 1L,
            pagingRequest = pagingRequest
        )

        return Response(
            data = paymentService.getPaymentList(request)
        )
    }

    @PostMapping
    fun pay(
        @RequestBody request: PaymentRequest
    ): Response<PaymentResponse> {
        return Response(
            data = paymentService.pay(request)
        )
    }

    @PutMapping
    fun unPay(
        @RequestBody request: UnPaymentRequest
    ): Response<UnPaymentResponse> {
        return Response(
            data = paymentService.unPay(request)
        )
    }
}
