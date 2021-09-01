package com.haphollys.booook.domains.user

import javax.persistence.*

@Table(name = "users")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String
) {
}