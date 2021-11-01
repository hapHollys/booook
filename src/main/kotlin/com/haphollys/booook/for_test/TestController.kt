package com.haphollys.booook.for_test

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class TestController(
    private val insertDataForTestService: InsertDataForTestService
) {
    @GetMapping("/static")
    fun forLoadTest(): String {
        return "OK"
    }

    @GetMapping("/test")
    fun insertForTest() {
        insertDataForTestService.testInsert()
    }

    @GetMapping("/test2")
    fun getTest(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss") date: LocalDateTime
    ): String {
        println("date : $date")
        return "OK"
    }

    @GetMapping("/test3")
    fun insertTestScreens(): String {
        insertDataForTestService.testScreenInsert()
        return "OK"
    }
}
