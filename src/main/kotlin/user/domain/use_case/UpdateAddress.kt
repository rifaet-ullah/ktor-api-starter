package com.example.user.domain.use_case

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.domain.UserError
import com.example.user.domain.models.Address
import com.example.user.domain.models.Profile
import com.example.user.domain.repository.UserRepository

class UpdateAddress(
    private val userRepository: UserRepository,
    private val profileRepository: Repository<Profile, UserError>,
    private val addressRepository: Repository<Address, UserError>,
) {
    suspend operator fun invoke(userId: Long, address: Address): Result<Address, UserError> {
        val user = when (val userResult = userRepository.getById(userId)) {
            is Result.Error -> return Result.Error(UserError.UNKNOWN)
            is Result.Ok -> userResult.data
        }

        val addressResult = if (user.profile.address == null) {
            addressRepository.add(address)
        } else {
            addressRepository.update(address.copy(id = user.profile.address.id))
        }
        val address = when (addressResult) {
            is Result.Error -> return Result.Error(UserError.UNKNOWN)
            is Result.Ok -> addressResult.data
        }

        if (user.profile.address == null) {
            val profileResult = profileRepository.update(
                user.profile.copy(address = address)
            )
            return when (profileResult) {
                is Result.Error -> Result.Error(UserError.UNKNOWN)
                is Result.Ok -> Result.Ok(address)
            }
        }

        return Result.Ok(address)
    }
}
