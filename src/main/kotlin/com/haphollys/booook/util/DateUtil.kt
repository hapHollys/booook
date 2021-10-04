package com.haphollys.booook.util

import java.time.LocalDateTime

class DateUtil {
    companion object {
        fun getStartTimeOfDay(date: LocalDateTime): LocalDateTime {
            return LocalDateTime.of(
                date.year, date.month, date.dayOfMonth, 0, 0
            )
        }

        fun getEndTimeOfDay(date: LocalDateTime): LocalDateTime {
            return LocalDateTime.of(
                date.year, date.month, date.dayOfMonth, 23, 59
            )
        }
    }
}
