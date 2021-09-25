package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.ScreenService
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.ScreenDto.GetBookableSeatsResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/screens")
class ScreenController(
    private val screenService: ScreenService
) {
    @GetMapping("/movies/{movieId}")
    fun getScreens(
        @PathVariable movieId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss") date: LocalDateTime,
        @ModelAttribute pagingRequest: PagingRequest
    ): Response<List<ScreenDto.GetScreenResponse>> {
        val request = ScreenDto.GetScreenRequest(
            movieId = movieId,
            date = date,
            pagingRequest = pagingRequest
        )

        return Response(
            data = screenService.getScreens(request)
        )
    }

    @GetMapping("/{screenId}/seats")
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