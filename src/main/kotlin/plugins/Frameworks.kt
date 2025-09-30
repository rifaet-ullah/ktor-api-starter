package com.example.plugins

import com.example.di.appRepositories
import com.example.di.appServices
import com.example.di.appUseCases
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            modules = listOf(
                appServices(),
                appRepositories(),
                appUseCases()
            )
        )
    }
}
