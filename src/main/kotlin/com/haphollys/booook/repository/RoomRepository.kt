package com.haphollys.booook.repository

import com.haphollys.booook.domains.room.RoomEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository: JpaRepository<RoomEntity, Long> {
}