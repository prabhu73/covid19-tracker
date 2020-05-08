package com.covidtracker.webservices

import com.covidtracker.dto.AllStateWiseResponse
import com.covidtracker.dto.StateDistrictsData
import retrofit2.Response
import retrofit2.http.GET

interface CovidWebservices {

    @GET("/data.json")
    suspend fun getStateWiseData(): Response<AllStateWiseResponse>

    @GET("/v2/state_district_wise.json")
    suspend fun getStateDistrictWiseData(): Response<List<StateDistrictsData>>

}