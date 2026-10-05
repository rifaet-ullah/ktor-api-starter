package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.Profile
import com.example.user.domain.models.User
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.PasswordService
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddUser(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val profileRepository: Repository<Profile, UserError>,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        email: String,
        password: String,
    ): Result<User, UserError> {
        val existingUserResult = userRepository.getByEmail(email)
        if (existingUserResult is Result.Ok) return Result.Error(UserError.USER_ALREADY_EXIST)

        val profileResult = profileRepository.add(
            item = Profile(
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth,
            )
        )
        if (profileResult is Result.Error) return Result.Error(UserError.UNKNOWN)

        val userResult = userRepository.add(
            User(
                profile = (profileResult as Result.Ok).data,
                email = email,
                password = passwordService.generate(password),
                dateJoined = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        return when (userResult) {
            is Result.Error -> Result.Error(UserError.UNKNOWN)
            is Result.Ok -> Result.Ok(userResult.data)
        }
    }
}
