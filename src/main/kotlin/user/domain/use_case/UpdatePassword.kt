package com.example.user.domain.use_case

import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.User
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.PasswordService

class UpdatePassword(private val userRepository: UserRepository, private val passwordService: PasswordService) {
    suspend operator fun invoke(userId: Long, oldPassword: String, newPassword: String): Result<User, UserError> {
        return when (val userResult = userRepository.getById(userId)) {
            is Result.Failure -> Result.Failure(UserError.NOT_FOUND)
            is Result.Success -> {
                val validPassword = passwordService.verify(
                    password = oldPassword, hashedPassword = userResult.data.password
                )
                if (!validPassword) return Result.Failure(UserError.WRONG_PASSWORD)

                val updatedUserResult = userRepository.update(
                    userResult.data.copy(password = passwordService.generate(newPassword))
                )
                when(updatedUserResult) {
                    is Result.Failure -> Result.Failure(UserError.UNKNOWN)
                    is Result.Success -> userResult
                }
            }
        }
    }
}
