package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.ApiResponse
import com.haphollys.booook.service.PaymentService
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.PaymentDto
import com.haphollys.booook.service.dto.PaymentDto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @GetMapping
    fun getPayments(
        @ModelAttribute pagingRequest: PagingRequest
    ): ResponseEntity<ApiResponse<List<PaymentDto.GetPaymentResponse>>> {
        val request = GetPaymentRequest(
            userId = 1L,
            pagingRequest = pagingRequest
        )

        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = paymentService.getPaymentList(request)
            )
        )
    }

    @PostMapping
    fun pay(
        @RequestBody request: PaymentRequest
    ): ResponseEntity<ApiResponse<PaymentResponse>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = paymentService.pay(request)
            )
        )
    }

    @PutMapping
    fun unPay(
        @RequestBody request: UnPaymentRequest
    ): ResponseEntity<ApiResponse<UnPaymentResponse>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = paymentService.unPay(request)
            )
        )
    }
}
