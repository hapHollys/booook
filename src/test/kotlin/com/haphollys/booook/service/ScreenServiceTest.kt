package com.haphollys.booook.service

import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import com.haphollys.booook.getTestScreenEntity
import com.haphollys.booook.repository.ScreenRepository
import com.haphollys.booook.service.dto.ScreenDto
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class ScreenServiceTest {

    lateinit var screenRepository: ScreenRepository

    lateinit var screenService: ScreenService

    lateinit var testUser: UserEntity
    lateinit var testScreen: ScreenEntity

    @BeforeEach
    fun setUp() {
        screenRepository = mockk()

        screenService = ScreenService(
            screenRepository = screenRepository
        )

        testUser = UserEntity(name = "TEST_USER")
        testScreen = spyk(getTestScreenEntity())
        testScreen.id = 1L
    }

    @Test
    fun `예약 가능한 좌석만 조회`() {
        // given
        every {
            screenRepository.findById(any())
        } returns Optional.of(testScreen)

        // when
        val request = ScreenDto.GetBookableSeatsRequest(testScreen.id!!)
        screenService.getBookableSeats(request)

        // then
        verify(atLeast = 1) {
            screenRepository.findById(1L)
        }

        verify(atLeast = 1) {
            testScreen.getBookableSeats()
        }
    }

}