package com.covidtracker.webservices

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import retrofit2.Response

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
abstract class NetworkFlowRepository<T> {

    fun asFlows() = flow<ResponseState<T>> {

        // Emit loading state
        emit(ResponseState.loading())

        try {

            // Fetch data from remote
            val apiResponse = fetchRemoteData()

            // Fetch response body
            val responseBody = apiResponse.body()

            if (apiResponse.isSuccessful && responseBody != null) {
                emit(ResponseState.success(responseBody))
            } else {
                emit(ResponseState.failed(apiResponse.message()))
            }

        } catch (e: Exception) {
            emit(ResponseState.failed("Remote data fetch error. Please try after some time."))
            e.printStackTrace()
        }
    }

    @MainThread
    protected abstract suspend fun fetchRemoteData(): Response<T>
}