package com.example.core.domain

sealed interface Result<D, E : Error> {
    data class Ok<D, E : com.example.core.domain.Error>(val data: D) : Result<D, E>
    data class Error<D, E : com.example.core.domain.Error>(val error: E) : Result<D, E>
}
