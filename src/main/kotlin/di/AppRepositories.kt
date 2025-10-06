package com.example.di

import com.example.core.domain.Repository
import com.example.user.data.repository.AddressRepositoryImpl
import com.example.user.data.repository.ProfileRepositoryImpl
import com.example.user.data.repository.UserRepositoryImpl
import com.example.user.data.table.AddressTable
import com.example.user.data.table.ProfileTable
import com.example.user.data.table.UserTable
import com.example.user.domain.AddressError
import com.example.user.domain.ProfileError
import com.example.user.domain.models.Address
import com.example.user.domain.models.Profile
import com.example.user.domain.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module

fun Application.appRepositories() = module {
    single<Database> {
        val driver = environment.config.property("database.driver").getAs<String>()
        val name = environment.config.property("database.name").getAs<String>()
        val user = environment.config.property("database.user").getAs<String>()
        val password = environment.config.property("database.password").getAs<String>()
        val host = environment.config.property("database.host").getAs<String>()
        val port = environment.config.property("database.port").getAs<Int>()

        val database = Database.connect(url = "jdbc:$driver://$host:$port/$name", user = user, password = password)

        transaction(database) {
            SchemaUtils.create(UserTable, ProfileTable, AddressTable)
            SchemaUtils.addMissingColumnsStatements(UserTable, ProfileTable, AddressTable)
        }

        database
    }
    single<Repository<Address, AddressError>> { AddressRepositoryImpl(database = get()) }
    single<Repository<Profile, ProfileError>> { ProfileRepositoryImpl(database = get()) }
    single<UserRepository> { UserRepositoryImpl(database = get()) }
}
