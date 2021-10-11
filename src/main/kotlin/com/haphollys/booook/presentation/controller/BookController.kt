package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.ApiResponse
import com.haphollys.booook.service.BookService
import com.haphollys.booook.service.dto.BookDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookService: BookService
) {
    @PostMapping()
    fun book(
        @RequestBody request: BookDto.BookRequest
    ): ResponseEntity<ApiResponse<BookDto.BookResponse>> {
        return ResponseEntity.ok().body(
                ApiResponse.success(
                    data = bookService.book(request)
                )
            )
    }
}
