package com.haphollys.booook.repository

import com.haphollys.booook.domains.screen.ScreenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ScreenRepository: JpaRepository<ScreenEntity, Long> {
}