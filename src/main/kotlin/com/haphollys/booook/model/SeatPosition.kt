package com.haphollys.booook.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class SeatPosition(
    @Column
    val row: Int,
    val col: Int
) {
    override fun equals(other: Any?): Boolean {
        val o = other as SeatPosition
        return row == o.row && col == o.col
    }
}