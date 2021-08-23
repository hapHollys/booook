package com.haphollys.booook.repository

import com.haphollys.booook.domains.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
}