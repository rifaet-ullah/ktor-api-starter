package com.example.user.domain

import com.example.core.domain.Error

enum class UserError: Error {
    NOT_FOUND,
    ALREADY_EXIST,
    UNKNOWN
}
