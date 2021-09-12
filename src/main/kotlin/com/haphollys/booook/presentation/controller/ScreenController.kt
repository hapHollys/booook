package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.ScreenService
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.ScreenDto.GetBookableSeatsResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/screens")
class ScreenController(
    private val screenService: ScreenService
) {
    @GetMapping("/{screenId}")
    fun getBookableSeats(
        @PathVariable("screenId") screenId: Long
    ): Response<GetBookableSeatsResponse> {
        return Response(
            data = screenService.getBookableSeats(
                ScreenDto.GetBookableSeatsRequest(
                    screenId = screenId
                )
            )
        )
    }
}