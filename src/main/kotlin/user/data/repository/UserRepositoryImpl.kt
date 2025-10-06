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
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl(private val database: Database) : UserRepository {
    override suspend fun getByEmail(email: String): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            UserEntity.find { UserTable.email eq email }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun add(item: User): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            val profileEntity = ProfileEntity.findById(item.profile.id) ?: return@newSuspendedTransaction null
            UserEntity.new {
                profile = profileEntity
                email = item.email
                password = item.password.value
                salt = item.password.salt
                role = item.role.name
                isActive = item.isActive
                dateJoined = item.dateJoined
                lastLogin = item.lastLogin
            }
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun getById(id: Long): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            UserEntity.find { UserTable.id eq id }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun update(item: User): Result<User, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            val profileEntity = ProfileEntity.findById(item.profile.id) ?: return@newSuspendedTransaction null
            UserEntity.findByIdAndUpdate(id = item.id) {
                it.profile = profileEntity
                it.email = item.email
                it.password = item.password.value
                it.salt = item.password.salt
                it.role = item.role.name
                it.isActive = item.isActive
                it.dateJoined = item.dateJoined
                it.lastLogin = item.lastLogin
            }
        } ?: return Result.Failure(UserError.NOT_FOUND)

        return Result.Success(data = entity.toUser())
    }

    override suspend fun delete(id: Long): Result<Unit, UserError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            UserEntity.find { UserTable.id eq id }.firstOrNull()
        } ?: return Result.Failure(UserError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
