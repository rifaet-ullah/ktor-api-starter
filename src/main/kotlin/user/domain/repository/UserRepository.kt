package com.example.user.domain.repository

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.User

interface UserRepository: Repository<User, UserError> {
    suspend fun getByEmail(email: String): Result<User, UserError>
}
