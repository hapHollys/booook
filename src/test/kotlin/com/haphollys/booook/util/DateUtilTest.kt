package com.haphollys.booook.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class DateUtilTest {
    val testYear = 2000
    val testMonth = 12
    val testDayOfMonth = 31
    val testHour = 9
    val testMin = 45

    val testDate = LocalDateTime.of(
        testYear,
        testMonth,
        testDayOfMonth,
        testHour,
        testMin
    )

    val dateutil = DateUtil()

    @Test
    fun `date의 시작 시간`() {
        // when
        val startTimeOfDay = DateUtil.getStartTimeOfDay(testDate)

        // then
        assertEquals(testYear, startTimeOfDay.year)
        assertEquals(testMonth, startTimeOfDay.monthValue)
        assertEquals(testDayOfMonth, startTimeOfDay.dayOfMonth)
        assertEquals(0, startTimeOfDay.hour)
        assertEquals(0, startTimeOfDay.minute)
    }

    @Test
    fun `date의 끝 시간`() {
        // when
        val endOfTimeOfDay = DateUtil.getEndTimeOfDay(testDate)

        // then
        assertEquals(testYear, endOfTimeOfDay.year)
        assertEquals(testMonth, endOfTimeOfDay.monthValue)
        assertEquals(testDayOfMonth, endOfTimeOfDay.dayOfMonth)
        assertEquals(23, endOfTimeOfDay.hour)
        assertEquals(59, endOfTimeOfDay.minute)
    }
}
