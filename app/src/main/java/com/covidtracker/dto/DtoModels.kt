package com.covidtracker.dto

import com.google.gson.annotations.SerializedName

/**
 * All states information
 */
data class AllStateWiseResponse(
    @SerializedName("statewise") var stateWiseData: List<StateWiseData>
)

data class StateWiseData(
    @SerializedName("active") val active: String,
    @SerializedName("confirmed") val confirmed: String,
    @SerializedName("deaths") val deaths: String,
    @SerializedName("deltaconfirmed") val deltaConfirmed: String,
    @SerializedName("deltadeaths") val deltaDeaths: String,
    @SerializedName("deltarecovered") val deltaRecovered: String,
    @SerializedName("lastupdatedtime") val lastUpdatedTime: String,
    @SerializedName("recovered") val recovered: String,
    @SerializedName("state") val state: String,
    @SerializedName("statecode") val stateCode: String
)

/**
 * All state districts data
 */
data class StateDistrictsData(
    @SerializedName("state") val state: String,
    @SerializedName("districtData") val districtData: List<DistrictData>
)

data class DistrictData(
    @SerializedName("district") val district: String,
    @SerializedName("confirmed") val confirmed: Int,
    @SerializedName("lastupdatedtime") val lastUpdatedTime: String,
    @SerializedName("delta") val deltaConfirmed: DistrictDeltaConfirmed
)

data class DistrictDeltaConfirmed(
    @SerializedName("confirmed") val confirmed: Int
)