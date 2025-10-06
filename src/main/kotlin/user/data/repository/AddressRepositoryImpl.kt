package com.example.user.data.repository

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.data.entity.AddressEntity
import com.example.user.data.entity.toAddress
import com.example.user.domain.AddressError
import com.example.user.domain.models.Address
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class AddressRepositoryImpl(private val database: Database) : Repository<Address, AddressError> {
    override suspend fun add(item: Address): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            AddressEntity.new {
                addressLine = item.addressLine
                city = item.city
                district = item.district
                division = item.division
                postalCode = item.postalCode
                country = item.country
            }
        }

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun getById(id: Long): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            AddressEntity.findById(id)
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun update(item: Address): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            AddressEntity.findByIdAndUpdate(id = item.id) {
                it.addressLine = item.addressLine
                it.city = item.city
                it.district = item.district
                it.division = item.division
                it.postalCode = item.postalCode
                it.country = item.country
            }
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun delete(id: Long): Result<Unit, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO, database) {
            AddressEntity.findById(id)
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
