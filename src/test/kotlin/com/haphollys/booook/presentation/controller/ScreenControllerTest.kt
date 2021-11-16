package com.haphollys.booook.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.getTestPriceList
import com.haphollys.booook.service.ScreenService
import com.haphollys.booook.service.SeatPreemptService
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.ScreenDto.*
import com.haphollys.booook.service.dto.SeatDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

@WebMvcTest(controllers = [ScreenController::class])
internal class ScreenControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var screenService: ScreenService

    @MockkBean
    lateinit var preemptService: SeatPreemptService

    val baseUrl = "/api/v1/screens"

    val targetYear = 2020
    val targetMonth = 12
    val targetDay = 1
    val targetHour = 1
    val targetMinute = 1

    val targetScreen = ScreenEntity.of(
        movie = mockk<MovieEntity>(relaxed = true).apply { id = 1L },
        room = mockk(relaxed = true),
        date = LocalDateTime.of(targetYear, targetMonth, targetDay, targetHour, targetMinute),
        priceTable = getTestPriceList().table
    ).apply { id = 1L }

    @Test
    fun `특정 영화의 특정 날짜의 상영목록 요청`() {
        // given
        val movieId = targetScreen.movie.id!!.toString()
        val requestDate = "$targetYear-0$targetMinute-0$targetDay 0$targetHour:0$targetMinute:00"

        val getScreensResponse = GetScreenResponse(
            screenId = targetScreen.id!!,
            screenDateTime = targetScreen.date,
            roomSeatNum = targetScreen.getNumSeats(),
            remainSeatNum = targetScreen.getBookableSeats().size,
            roomType = targetScreen.screenRoom.roomType
        )

        every {
            screenService.getScreens(any())
        } returns listOf(getScreensResponse)

        // when, then
        mvc.get(baseUrl + "/movies/$movieId") {
            param("movieId", movieId)
            param("date", requestDate)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `예약 가능한 좌석 목록 요청`() {
        // given
        val response = GetBookableSeatsResponse(
            listOf(
                SeatDto(row = 0, col = 0, type = FRONT),
                SeatDto(row = 0, col = 1, type = FRONT)
            )
        )

        every {
            screenService.getBookableSeats(any())
        } returns response

        // when, then
        mvc.get(baseUrl + "/${targetScreen.id}/seats")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `좌석 선점 요청`() {
        // given
        val request = PreemptSeatsRequest(
            seats = listOf(
                SeatDto(0, 0, FRONT)
            )
        )

        every {
            preemptService.preemptSeats(any())
        } returns PreemptSeatsResponse(true)

        // when, then
        mvc.put(baseUrl + "/${targetScreen.id}/seats") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
            }
    }
}
