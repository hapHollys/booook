package com.haphollys.booook.service

import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.ScreenDto.*
import com.haphollys.booook.service.dto.SeatDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class ScreenService(
    private val screenRepository: ScreenRepository,
) {
    fun getBookableSeats(
        getBookableSeatsRequest: GetBookableSeatsRequest
    ): GetBookableSeatsResponse {
        val foundScreen = screenRepository.findById(getBookableSeatsRequest.screenId)
            .orElseThrow {
                EntityNotFoundException("해당 스크린이 없습니다.")
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

    fun getScreens(
        request: ScreenDto.GetScreenRequest
    ): List<GetScreenResponse> {
        val foundScreens = screenRepository.findByMovieIdAndDate(
            movieId = request.movieId!!,
            date = request.date,
            pagingRequest = request.pagingRequest
        )

        return foundScreens.map {
            GetScreenResponse(
                screenId = it.id!!,
                screenDateTime = it.date,
                roomSeatNum = it.getSeatNum(),
                remainSeatNum = it.getBookableSeats().size,
                roomType = it.screenRoom.roomType
            )
        }
    }
}
