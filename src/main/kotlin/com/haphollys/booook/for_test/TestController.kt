package com.haphollys.booook.for_test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val insertDataForTestService: InsertDataForTestService
) {
    @GetMapping("/test")
    fun insertForTest() {
        insertDataForTestService.testInsert()
    }
}