package com.example.core.domain

interface Repository<T, E: Error> {
    suspend fun add(data: T): Result<T, E>
    suspend fun getById(id: Long): Result<T, E>
    suspend fun update(data: T): Result<T, E>
    suspend fun delete(id: Long): Result<Unit, E>
}
