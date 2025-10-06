package com.example.user.domain

import com.example.core.domain.Error

enum class ProfileError: Error {
    NOT_FOUND,
    ALREADY_EXIST,
    UNKNOWN,
}
