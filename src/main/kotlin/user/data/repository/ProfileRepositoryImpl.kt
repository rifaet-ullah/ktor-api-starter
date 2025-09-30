package com.example.user.data.repository

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.data.entity.AddressEntity
import com.example.user.data.entity.ProfileEntity
import com.example.user.data.entity.toProfile
import com.example.user.data.table.ProfileTable
import com.example.user.domain.ProfileError
import com.example.user.domain.models.Profile
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ProfileRepositoryImpl(database: Database) : Repository<Profile, ProfileError> {

    init {
        transaction(database) {
            SchemaUtils.create(ProfileTable)
            SchemaUtils.addMissingColumnsStatements(ProfileTable)
        }
    }

    override suspend fun add(data: Profile): Result<Profile, ProfileError> {
        val dao = newSuspendedTransaction(Dispatchers.IO) {
            ProfileEntity.new {
                firstName = data.firstName
                lastName = data.lastName
                dateOfBirth = data.dateOfBirth
                address = data.address?.let { address -> AddressEntity.findById(address.id) }
                pictureUri = data.pictureUri
            }
        }

        return Result.Success(data = dao.toProfile())
    }

    override suspend fun getById(id: Long): Result<Profile, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            ProfileEntity.findById(id)
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        return Result.Success(data = entity.toProfile())
    }

    override suspend fun update(data: Profile): Result<Profile, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            ProfileEntity.findByIdAndUpdate(id = data.id) {
                it.firstName = data.firstName
                it.lastName = data.lastName
                it.dateOfBirth = data.dateOfBirth
                it.address = data.address?.let { address -> AddressEntity.findById(address.id) }
                it.pictureUri = data.pictureUri
            }
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        return Result.Success(data = entity.toProfile())
    }

    override suspend fun delete(id: Long): Result<Unit, ProfileError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            ProfileEntity.findById(id)
        } ?: return Result.Failure(ProfileError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
