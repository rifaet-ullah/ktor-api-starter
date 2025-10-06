package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.AddressError
import com.example.user.domain.ProfileError
import com.example.user.domain.models.Address
import com.example.user.domain.models.Profile
import com.example.user.domain.repository.UserRepository

class UpdateProfile(
    private val userRepository: UserRepository,
    private val profileRepository: Repository<Profile, ProfileError>,
    private val addressRepository: Repository<Address, AddressError>,
) {
    suspend operator fun invoke(userId: Long, profile: Profile): Result<Profile, ProfileError> {
        return when (val userResult = userRepository.getById(userId)) {
            is Result.Failure -> Result.Failure(ProfileError.NOT_FOUND)
            is Result.Success -> {
                val address = if (profile.address == null) {
                    userResult.data.profile.address
                } else {
                    val addressResult = if (userResult.data.profile.address == null) {
                        addressRepository.add(profile.address)
                    } else {
                        addressRepository.update(profile.address.copy(id = userResult.data.profile.address.id))
                    }

                    when (addressResult) {
                        is Result.Failure -> null
                        is Result.Success -> addressResult.data
                    }
                }

                profileRepository.update(
                    profile.copy(id = userResult.data.profile.id, address = address)
                )
            }
        }
    }
}
