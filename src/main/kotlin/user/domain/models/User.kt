package com.example.user.domain.models

import kotlinx.datetime.LocalDateTime

data class User(
    val id: Long = -1,
    val profile: Profile,
    val email: String,
    val password: Password,
    val role: Role = Role.MEMBER,
    val isActive: Boolean = false,
    val dateJoined: LocalDateTime,
    val lastLogin: LocalDateTime? = null,
)
