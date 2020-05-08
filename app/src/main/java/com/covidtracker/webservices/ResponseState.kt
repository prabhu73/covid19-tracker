package com.covidtracker.webservices

sealed class ResponseState<T> {
    class Loading<T>: ResponseState<T>()
    data class Success<T>(val data: T): ResponseState<T>()
    data class Failed<T>(val data: String): ResponseState<T>()

    companion object {

        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success(data)

        fun <T> failed(data: String) = Failed<T>(data)
    }
}