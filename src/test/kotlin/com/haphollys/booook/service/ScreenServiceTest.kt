package com.haphollys.booook.service

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ScreenServiceTest {

    lateinit var screenService: ScreenService

    @BeforeEach
    fun setUp() {
        screenService = ScreenService()
    }

    @Test
    fun `예약 가능한 좌석만 조회`() {

    }

}