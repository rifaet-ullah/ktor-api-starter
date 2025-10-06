package com.example.user.data.repository

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.data.entity.AddressEntity
import com.example.user.data.entity.ProfileEntity
import com.example.user.data.entity.toProfile
import com.example.user.domain.ProfileError
import com.example.user.domain.models.Profile
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ProfileRepositoryImpl(private val database: Database) : Repository<Profile, ProfileError> {
    override suspend fun add(item: Profile): Result<Profile, ProfileError> {
        val dao = newSuspendedTransaction(Dispatchers.IO, database) {
            ProfileEntity.new {
                firstName = item.firstName
                lastName = item.lastName
                dateOfBirth = item.dateOfBirth
                address = item.address?.let { address -> AddressEntity.findById(address.id) }
                pictureUri = item.pictureUri
            }
        }

        return Result.Success(data = dao.toProfile())
    }

    override suspend fun getById(id: Long): Result<Profile, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            ProfileEntity.findById(id)
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        return Result.Success(data = entity.toProfile())
    }

    override suspend fun update(item: Profile): Result<Profile, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            ProfileEntity.findByIdAndUpdate(id = item.id) {
                it.firstName = item.firstName
                it.lastName = item.lastName
                it.dateOfBirth = item.dateOfBirth
                it.address = item.address?.let { address -> AddressEntity.findById(address.id) }
                it.pictureUri = item.pictureUri
            }
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        return Result.Success(data = entity.toProfile())
    }

    override suspend fun delete(id: Long): Result<Unit, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            ProfileEntity.findById(id)
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
