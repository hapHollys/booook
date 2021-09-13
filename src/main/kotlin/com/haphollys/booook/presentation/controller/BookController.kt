package com.haphollys.booook.presentation.controller

import com.haphollys.booook.presentation.Response
import com.haphollys.booook.service.BookService
import com.haphollys.booook.service.dto.BookDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookService: BookService
){
    @PostMapping()
    fun book(
        @RequestBody request: BookDto.BookRequest
    ): Response<BookDto.BookResponse> {
        return Response(
            data = bookService.book(request)
        )
    }
}