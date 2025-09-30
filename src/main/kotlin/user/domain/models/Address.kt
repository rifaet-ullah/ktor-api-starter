package com.example.user.domain.models

data class Address(
    val id: Long = -1,
    val addressLine: String,
    val city: String,
    val district: String,
    val division: String,
    val postalCode: Int,
    val country: String,
)
