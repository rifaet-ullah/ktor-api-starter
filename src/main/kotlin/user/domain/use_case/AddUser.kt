package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.ProfileError
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
    private val profileRepository: Repository<Profile, ProfileError>,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        email: String,
        password: String,
    ): Result<User, UserError> {
        return when (userRepository.getByEmail(email)) {
            is Result.Success -> Result.Failure(UserError.ALREADY_EXIST)
            is Result.Failure -> {
                val profileResult = profileRepository.add(
                    item = Profile(
                        firstName = firstName,
                        lastName = lastName,
                        dateOfBirth = dateOfBirth,
                    )
                )
                when(profileResult) {
                    is Result.Failure -> Result.Failure(UserError.UNKNOWN)
                    is Result.Success -> {
                        userRepository.add(
                            User(
                                profile = profileResult.data,
                                email = email,
                                password = passwordService.generate(password),
                                dateJoined = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                        )
                    }
                }
            }
        }
    }
}
