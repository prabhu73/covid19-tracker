package com.covidtracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.covidtracker.dto.AllStateWiseResponse
import com.covidtracker.dto.StateDistrictsData
import com.covidtracker.repository.MainRepository
import com.covidtracker.webservices.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainViewModel @ExperimentalCoroutinesApi constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _stateWiseLiveData = MutableLiveData<ResponseState<AllStateWiseResponse>>()
    val stateWiseLiveData: LiveData<ResponseState<AllStateWiseResponse>>
        get() = _stateWiseLiveData

    private val _districtWiseLiveData = MutableLiveData<ResponseState<List<StateDistrictsData>>>()
    val districtWiseLiveData: LiveData<ResponseState<List<StateDistrictsData>>>
        get() = _districtWiseLiveData

    private var _stateName: String? = null
    var stateName: String?
        get() = _stateName
        set(value) {
            _stateName = value
        }

    fun getStateData() {
        viewModelScope.launch {
            repository.getStateWiseData().collect {
                _stateWiseLiveData.value = it
            }
        }
    }

    fun getDistrictData() {
        viewModelScope.launch {
            stateName?.let { state ->
                repository.getStateDistrictWiseData(state).collect {
                    _districtWiseLiveData.value = it
                }
            }
        }
    }
}
