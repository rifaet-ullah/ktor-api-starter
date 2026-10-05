package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.Address
import com.example.user.domain.models.Profile
import com.example.user.domain.repository.UserRepository

class UpdateProfile(
    private val userRepository: UserRepository,
    private val profileRepository: Repository<Profile, UserError>,
    private val addressRepository: Repository<Address, UserError>,
) {
    suspend operator fun invoke(userId: Long, profile: Profile): Result<Profile, UserError> {
        val user = when (val userResult = userRepository.getById(userId)) {
            is Result.Error -> return Result.Error(UserError.PROFILE_NOT_FOUND)
            is Result.Ok -> userResult.data
        }

        val address = if (profile.address == null) {
            user.profile.address
        } else {
            val addressResult = if (user.profile.address == null) {
                addressRepository.add(profile.address)
            } else {
                addressRepository.update(profile.address.copy(id = user.profile.address.id))
            }

            when (addressResult) {
                is Result.Error -> null
                is Result.Ok -> addressResult.data
            }
        }

        val profileUpdateResult = profileRepository.update(
            profile.copy(id = user.profile.id, address = address)
        )
        return when (profileUpdateResult) {
            is Result.Error -> Result.Error(UserError.UNKNOWN)
            is Result.Ok -> Result.Ok(profileUpdateResult.data)
        }
    }
}
