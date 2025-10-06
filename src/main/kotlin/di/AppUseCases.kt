package com.example.di

import com.example.user.domain.use_case.AddUser
import com.example.user.domain.use_case.UpdateAddress
import com.example.user.domain.use_case.UpdatePassword
import com.example.user.domain.use_case.UpdateProfile
import org.koin.dsl.module

fun appUseCases() = module {
    factory { AddUser(userRepository = get(), passwordService = get(), profileRepository = get()) }
    factory { UpdateAddress(userRepository = get(), profileRepository = get(), addressRepository = get()) }
    factory { UpdateProfile(userRepository = get(), profileRepository = get(), addressRepository = get()) }
    factory { UpdatePassword(userRepository = get(), passwordService = get()) }
}
