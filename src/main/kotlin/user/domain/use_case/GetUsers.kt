package com.example.user.domain.use_case

import com.example.user.domain.repository.UserRepository

class GetUsers(private val userRepository: UserRepository) {

    suspend operator fun invoke(pageNumber: Int = 0, itemsPerPage: Int = 10) =
        userRepository.getAll(pageNumber, itemsPerPage)
}
