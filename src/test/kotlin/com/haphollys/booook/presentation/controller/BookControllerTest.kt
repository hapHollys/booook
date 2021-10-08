package com.haphollys.booook.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.BookService
import com.haphollys.booook.service.dto.BookDto
import com.haphollys.booook.service.dto.BookDto.BookRequest
import com.haphollys.booook.service.dto.SeatDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.http.server.reactive.MockServerHttpRequest.post
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [BookController::class])
internal class BookControllerTest() {
    @Autowired
    lateinit var mvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var bookService: BookService

    val baseUrl = "/api/v1/books"

    val bookRequest = BookRequest(
        userId = 1L,
        screenId = 1L,
        seats = listOf(
            SeatDto(
                row = 0,
                col = 0,
                type = FRONT
            )
        )
    )

    @Test
    fun `예약 성공`() {
        // given
        val response = BookDto.BookResponse(bookId = 1L)
        every {
            bookService.book(request = bookRequest)
        } returns response

        // when
        mvc.post(baseUrl) {
            content = objectMapper.writeValueAsString(bookRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                json(
                    objectMapper.writeValueAsString(Response(data = response))
                )
            }
        }
    }
}
