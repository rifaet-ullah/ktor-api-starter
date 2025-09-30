package com.example.di

import io.ktor.server.application.Application
import io.ktor.server.config.getAs
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

fun Application.appRepositories() = module {
    single<Database> {
        val driver = environment.config.property("database.driver").getAs<String>()
        val name = environment.config.property("database.name").getAs<String>()
        val user = environment.config.property("database.user").getAs<String>()
        val password = environment.config.property("database.password").getAs<String>()
        val host = environment.config.property("database.host").getAs<String>()
        val port = environment.config.property("database.port").getAs<Int>()

        Database.connect(
            url = "jdbc:$driver://$host:$port/$name",
            user = user,
            password = password
        )
    }
}
