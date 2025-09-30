package com.example.user.data.repository

import com.example.core.domain.Result
import com.example.user.data.entity.ProfileEntity
import com.example.user.data.entity.UserEntity
import com.example.user.data.entity.toUser
import com.example.user.data.table.UserTable
import com.example.user.domain.UserError
import com.example.user.domain.models.User
import com.example.user.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {
    override suspend fun getByEmail(email: String): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.find { UserTable.email eq email }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun add(data: User): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            val profileEntity = ProfileEntity.findById(data.profile.id) ?: return@newSuspendedTransaction null
            UserEntity.new {
                profile = profileEntity
                email = data.email
                password = data.password.value
                salt = data.password.salt
                role = data.role.name
                isActive = data.isActive
                dateJoined = data.dateJoined
                lastLogin = data.lastLogin
            }
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun getById(id: Long): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.find { UserTable.id eq id }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun update(data: User): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            val profileEntity = ProfileEntity.findById(data.profile.id) ?: return@newSuspendedTransaction null
            UserEntity.findByIdAndUpdate(id = data.id) {
                it.profile = profileEntity
                it.email = data.email
                it.password = data.password.value
                it.salt = data.password.salt
                it.role = data.role.name
                it.isActive = data.isActive
                it.dateJoined = data.dateJoined
                it.lastLogin = data.lastLogin
            }
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun delete(id: Long): Result<Unit, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.find { UserTable.id eq id }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
