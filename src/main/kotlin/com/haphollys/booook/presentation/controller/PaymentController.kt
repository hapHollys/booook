package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.PaymentService
import com.haphollys.booook.service.dto.PaymentDto.PaymentRequest
import com.haphollys.booook.service.dto.PaymentDto.PaymentResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping
    fun pay(
        @RequestBody request: PaymentRequest
    ): Response<PaymentResponse> {
        return Response(
            data = paymentService.pay(request)
        )
    }
}