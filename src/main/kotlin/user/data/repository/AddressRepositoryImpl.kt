package com.example.user.data.repository

import com.example.core.domain.Repository
import com.example.core.domain.Result
import com.example.user.data.entity.AddressEntity
import com.example.user.data.entity.toAddress
import com.example.user.data.table.AddressTable
import com.example.user.domain.AddressError
import com.example.user.domain.models.Address
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class AddressRepositoryImpl(database: Database) : Repository<Address, AddressError> {

    init {
        transaction (database){
            SchemaUtils.create(AddressTable)
            SchemaUtils.addMissingColumnsStatements(AddressTable)
        }
    }

    override suspend fun add(data: Address): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            AddressEntity.new {
                addressLine = data.addressLine
                city = data.city
                district = data.district
                division = data.division
                postalCode = data.postalCode
                country = data.country
            }
        }

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun getById(id: Long): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            AddressEntity.findById(id)
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun update(data: Address): Result<Address, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            AddressEntity.findByIdAndUpdate(id = data.id) {
                it.addressLine = data.addressLine
                it.city = data.city
                it.district = data.district
                it.division = data.division
                it.postalCode = data.postalCode
                it.country = data.country
            }
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        return Result.Success(data = entity.toAddress())
    }

    override suspend fun delete(id: Long): Result<Unit, AddressError> {
        val entity = newSuspendedTransaction(Dispatchers.IO) {
            AddressEntity.findById(id)
        } ?: return Result.Failure(AddressError.NOT_FOUND)

        entity.delete()
        return Result.Success(Unit)
    }
}
