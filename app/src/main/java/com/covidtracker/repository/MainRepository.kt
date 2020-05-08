package com.covidtracker.repository

import com.covidtracker.dto.AllStateWiseResponse
import com.covidtracker.dto.StateDistrictsData
import com.covidtracker.webservices.CovidWebservices
import com.covidtracker.webservices.NetworkFlowRepository
import com.covidtracker.webservices.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import retrofit2.Response

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MainRepository(private val apiService: CovidWebservices) {

    fun getStateWiseData(): Flow<ResponseState<AllStateWiseResponse>> {
        return object : NetworkFlowRepository<AllStateWiseResponse>() {
            override suspend fun fetchRemoteData(): Response<AllStateWiseResponse> =
                apiService.getStateWiseData()
        }.asFlows().transform<ResponseState<AllStateWiseResponse>, ResponseState<AllStateWiseResponse>> { value ->
            if (value is ResponseState.Success) {
                value.data.stateWiseData = value.data.stateWiseData
                    .dropLastWhile { it.confirmed.equals("0",false)}
                emit(ResponseState.success(value.data))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStateDistrictWiseData(stateName: String): Flow<ResponseState<List<StateDistrictsData>>> {
        return object : NetworkFlowRepository<List<StateDistrictsData>>() {
            override suspend fun fetchRemoteData(): Response<List<StateDistrictsData>> =
                apiService.getStateDistrictWiseData()
        }.asFlows()
            .transform<ResponseState<List<StateDistrictsData>>, ResponseState<List<StateDistrictsData>>> { value ->
                if (value is ResponseState.Success)
                    emit(ResponseState.success(arrayListOf(value.data.find { stateData ->
                        stateData.state == stateName
                    }) as List<StateDistrictsData>))
            }.flowOn(Dispatchers.IO)
    }
}
