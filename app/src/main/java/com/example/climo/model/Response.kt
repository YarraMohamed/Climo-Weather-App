package com.example.climo.model

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Failure(val err: Throwable) : Response<Nothing>()
}
