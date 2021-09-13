package com.haphollys.booook.service

import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.service.dto.ScreenDto.GetBookableSeatsRequest
import com.haphollys.booook.service.dto.ScreenDto.GetBookableSeatsResponse
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScreenService(
    private val screenRepository: ScreenRepository,
) {
    fun getBookableSeats(
        getBookableSeatsRequest: GetBookableSeatsRequest
    ): GetBookableSeatsResponse {
        val foundScreen = screenRepository.findById(getBookableSeatsRequest.screenId)
            .orElseThrow {
                RuntimeException("해당 스크린이 없습니다.")
            }

        val foundSeats = foundScreen.getBookableSeats()
        return GetBookableSeatsResponse(
            seats = foundSeats.map {
                SeatDto(
                    row = it.seatPosition.x,
                    col = it.seatPosition.y,
                    type = it.seatType
                )
            }
        )
    }
}