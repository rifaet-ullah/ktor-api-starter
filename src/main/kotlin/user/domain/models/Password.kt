package com.example.user.domain.models

data class Password(
    val value: String,
    val salt: String,
)
