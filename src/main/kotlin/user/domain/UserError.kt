package com.example.user.domain

import com.example.core.domain.Error

enum class UserError : Error {
    USER_NOT_FOUND,
    USER_ALREADY_EXIST,
    ADDRESS_NOT_FOUND,
    PROFILE_NOT_FOUND,
    PROFILE_ALREADY_EXIST,
    UNKNOWN,
    INVALID_PASSWORD,
    WRONG_PASSWORD
}
