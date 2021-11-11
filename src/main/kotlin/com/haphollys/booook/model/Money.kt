package com.haphollys.booook.model

import java.lang.IllegalArgumentException
import java.math.BigDecimal

class Money(
    var amount: BigDecimal,
) {
    fun plus(money: Money): Money {
        return Money(this.amount.add(money.amount))
    }

    fun minus(money: Money): Money {
        if (this.amount.minus(money.amount) < BigDecimal.ZERO)
            throw IllegalArgumentException("금액이 0보다 작습니다.")

        return Money(this.amount.minus(money.amount))
    }

    fun mul(mul: BigDecimal): Money {
        return Money(this.amount.times(mul))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        return amount.hashCode()
    }

    companion object {
        fun of(amount: Long): Money {
            return Money(BigDecimal.valueOf(amount))
        }
    }
}