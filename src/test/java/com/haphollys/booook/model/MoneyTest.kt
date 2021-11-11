package com.haphollys.booook.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class MoneyTest {
    @Test
    fun `2000원 플러스 3000원은 5000원`() {
        // given
        val _2000 = Money.of(2000)
        val _3000 = Money.of(3000)

        // when
        val result = _2000.plus(_3000)

        // then
        assertEquals(result,Money.of(5000L))
    }
}