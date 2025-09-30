package com.example.user.domain.service

import com.example.user.domain.models.Password

interface PasswordService {
    fun generate(password: String): Password
    fun verify(password: String, hashedPassword: Password): Boolean
}
