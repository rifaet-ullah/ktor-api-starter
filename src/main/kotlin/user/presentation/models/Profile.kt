package com.example.user.presentation.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
class Profile(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
)
