package com.example.user.domain.use_case

import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.User
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.PasswordService

class UpdatePassword(private val userRepository: UserRepository, private val passwordService: PasswordService) {
    suspend operator fun invoke(userId: Long, oldPassword: String, newPassword: String): Result<User, UserError> {
        val user = when (val userResult = userRepository.getById(userId)) {
            is Result.Error -> return Result.Error(UserError.USER_NOT_FOUND)
            is Result.Ok -> userResult.data
        }

        val validPassword = passwordService.verify(
            password = oldPassword, hashedPassword = user.password
        )
        if (!validPassword) return Result.Error(UserError.WRONG_PASSWORD)

        val updatedUserResult = userRepository.update(
            user.copy(password = passwordService.generate(newPassword))
        )
        return when (updatedUserResult) {
            is Result.Error -> Result.Error(UserError.UNKNOWN)
            is Result.Ok -> Result.Ok(user)
        }
    }
}
