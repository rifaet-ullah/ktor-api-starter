package com.example.user.presentation.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class User (
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("address") val address: String,
    @SerialName("dateOfBirth") val dateOfBirth: String,
)
