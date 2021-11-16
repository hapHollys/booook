package com.haphollys.booook.service

import com.haphollys.booook.domains.screen.Seat.SeatType.FRONT
import com.haphollys.booook.service.dto.ScreenDto
import com.haphollys.booook.service.dto.SeatDto
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.core.RedisTemplate

@ExtendWith(MockKExtension::class)
internal class SeatPreemptServiceTest {

    lateinit var redisTemplate: RedisTemplate<String, String>

    lateinit var seatPreemptService: SeatPreemptService

    @BeforeEach
    fun setUp() {
        redisTemplate = mockk(relaxed = true)
        seatPreemptService = spyk(
            SeatPreemptService(
                redisTemplate = redisTemplate
            )
        )
    }

    @Test
    fun `좌석 선점`() {
        // given
        val request = makeRequest()
        every {
            redisTemplate.opsForValue().multiSetIfAbsent(any())
        } returns true

        // when
        seatPreemptService.preemptSeats(
            request = request
        )

        // then
        val slot = slot<Map<String, String>>()
        verify(atLeast = 1) {
            redisTemplate.opsForValue().multiSetIfAbsent(capture(slot))
        }
        assertEquals(slot.captured.size, request.seats.size)

        verify(exactly = request.seats.size) {
            redisTemplate.expire(any(), any(), any())
        }
    }

    @Test
    fun `이미 선점된 경우 실패`() {
        // given
        val request = makeRequest()
        every {
            redisTemplate.opsForValue().multiSetIfAbsent(any())
        } returns false

        // when, then
        assertThrows<IllegalArgumentException> {
            seatPreemptService.preemptSeats(
                request = request
            )
        }
    }

    private fun makeRequest(
        userId: Long? = 1L,
        screenId: Long? = 1L,
        seats: List<SeatDto> = listOf()
    ) = ScreenDto.PreemptSeatsRequest(
        screenId = screenId,
        seats = listOf(
            SeatDto(0, 0, FRONT),
            SeatDto(0, 1, FRONT)
        )
    )
}