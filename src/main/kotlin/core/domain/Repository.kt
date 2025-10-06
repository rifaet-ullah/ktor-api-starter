package com.example.core.domain

interface Repository<T, E: Error> {
    suspend fun add(item: T): Result<T, E>
    suspend fun getById(id: Long): Result<T, E>
    suspend fun update(item: T): Result<T, E>
    suspend fun delete(id: Long): Result<Unit, E>
}
