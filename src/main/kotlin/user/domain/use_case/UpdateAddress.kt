package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.AddressError
import com.example.user.domain.ProfileError
import com.example.user.domain.UserError
import com.example.user.domain.models.Address
import com.example.user.domain.models.Profile
import com.example.user.domain.repository.UserRepository

class UpdateAddress(
    private val userRepository: UserRepository,
    private val profileRepository: Repository<Profile, ProfileError>,
    private val addressRepository: Repository<Address, AddressError>,
) {
    suspend operator fun invoke(userId: Long, address: Address): Result<Address, UserError> {
        return when (val userResult = userRepository.getById(userId)) {
            is Result.Failure -> Result.Failure(UserError.UNKNOWN)
            is Result.Success -> {
                val addressResult = if (userResult.data.profile.address == null) {
                    addressRepository.add(address)
                } else {
                    addressRepository.update(address.copy(id = userResult.data.profile.address.id))
                }

                when (addressResult) {
                    is Result.Failure -> Result.Failure(UserError.UNKNOWN)
                    is Result.Success -> {
                        if (userResult.data.profile.address == null) {
                            val profileResult = profileRepository.update(
                                userResult.data.profile.copy(address = addressResult.data)
                            )
                            when (profileResult) {
                                is Result.Failure -> Result.Failure(UserError.UNKNOWN)
                                is Result.Success -> Result.Success(addressResult.data)
                            }
                        } else {
                            Result.Success(addressResult.data)
                        }
                    }
                }
            }
        }
    }
}
