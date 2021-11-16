package com.haphollys.booook.service

import com.haphollys.booook.model.SeatPosition
import com.haphollys.booook.service.dto.ScreenDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SeatPreemptService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    // 잠금을 요청하고 만약 불가능 하다면 throw Exception
    // 여러 좌석 잠금 시.. 루아 스크립트로 원자성 보장 받자
    // 여러 좌석 잠금 시 수행하다가 롤백보다, 이를 먼저 확인할 방법이 필요, (lua script, memsetnx)
    fun preemptSeats(
        request: ScreenDto.PreemptSeatsRequest
    ): ScreenDto.PreemptSeatsResponse{
        // 올바른 범위의 좌석
        // verifySeatPositions(request.seats)

        val keyMaps = request.seats.map {
            createKey(screenId = request.screenId!!, position = it.toSeatPosition()) to EMPTY_VALUE
        }.toMap()

        // 논블록킹 비동기 처리로 최적화 가능할 듯
        val result = redisTemplate.opsForValue().multiSetIfAbsent(keyMaps)!!
        verifyPreemptible(result)

        keyMaps.forEach {
            redisTemplate.expire(it.key, EXPIRE_TIME, EXPIRE_TIME_UNIT)
        }

        return ScreenDto.PreemptSeatsResponse(
            preempted = result
        )
    }

    internal fun createKey(
        screenId: Long,
        position: SeatPosition
    ): String {
        return "$screenId:${position.x}:${position.y}"
    }

    internal fun verifyPreemptible(result: Boolean) {
        if (!result) throw IllegalArgumentException("이미 예매된 좌석입니다.")
    }

    companion object {
        val EXPIRE_TIME_UNIT = TimeUnit.MINUTES
        const val EXPIRE_TIME = 30L
        const val EMPTY_VALUE = "0"
    }
}