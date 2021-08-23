package com.haphollys.booook.repository

import com.haphollys.booook.domains.book.BookEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<BookEntity, Long> {
}


