package com.haphollys.booook.model

import javax.persistence.Embeddable

@Embeddable
class SeatPosition(
    var x: Int,
    var y: Int
) {
    override fun equals(other: Any?): Boolean {
        val o = other as SeatPosition
        return x == o.x && y == o.y
    }
}