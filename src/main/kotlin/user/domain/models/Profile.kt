package com.example.user.domain.models

import kotlinx.datetime.LocalDate

data class Profile(
    val id: Long = -1,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val address: Address? = null,
    val pictureUri: String? = null,
)
