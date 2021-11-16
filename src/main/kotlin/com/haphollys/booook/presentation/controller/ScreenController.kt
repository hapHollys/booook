package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.ApiResponse
import com.haphollys.booook.service.ScreenService
import com.haphollys.booook.service.SeatPreemptService
import com.haphollys.booook.service.dto.PagingRequest
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.ScreenDto.GetBookableSeatsResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/screens")
class ScreenController(
    private val screenService: ScreenService,
    private val preemptService: SeatPreemptService
) {
    @GetMapping("/movies/{movieId}")
    fun getScreens(
        @PathVariable movieId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss") date: LocalDateTime,
        @ModelAttribute pagingRequest: PagingRequest
    ): ResponseEntity<ApiResponse<List<ScreenDto.GetScreenResponse>>> {
        val request = ScreenDto.GetScreenRequest(
            movieId = movieId,
            date = date,
            pagingRequest = pagingRequest
        )

        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = screenService.getScreens(request)
            )
        )
    }

    @GetMapping("/{screenId}/seats")
    fun getBookableSeats(
        @PathVariable("screenId") screenId: Long
    ): ResponseEntity<ApiResponse<GetBookableSeatsResponse>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = screenService.getBookableSeats(
                    ScreenDto.GetBookableSeatsRequest(
                        screenId = screenId
                    )
                )
            )
        )
    }

    @PutMapping("/{screenId}/seats")
    fun preemptSeats(
        @PathVariable screenId: Long,
        @RequestBody @Valid request: ScreenDto.PreemptSeatsRequest
    ): ResponseEntity<ApiResponse<ScreenDto.PreemptSeatsResponse>> {
        request.screenId = screenId

        return ResponseEntity.ok().body(
            ApiResponse.success(
                data = preemptService.preemptSeats(
                    request = request
                )
            )
        )
    }
}
