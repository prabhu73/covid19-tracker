package com.covidtracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidtracker.databinding.FragmentStatesDataBinding
import com.covidtracker.dto.AllStateWiseResponse
import com.covidtracker.ui.adapters.OnStateItemSelection
import com.covidtracker.ui.adapters.StatesDataAdapter
import com.covidtracker.webservices.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StatesDataFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnStateItemSelection {

    private lateinit var viewBinder: FragmentStatesDataBinding
    private val viewModel: MainViewModel by sharedViewModel()
    private val statesAdapter = StatesDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = FragmentStatesDataBinding.inflate(layoutInflater)
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinder.stateDataRefresher.setOnRefreshListener(this)
        statesAdapter.setStateSelectionListener(this)
        viewBinder.stateWiseDetailsList.adapter = statesAdapter
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getStateData()
        viewModel.stateWiseLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ResponseState.Success -> setUiData(it.data)
                is ResponseState.Loading -> viewBinder.stateDataRefresher.isRefreshing = true
                is ResponseState.Failed -> viewBinder.stateDataRefresher.isRefreshing = false
            }
        })

        viewBinder.backNavIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUiData(data: AllStateWiseResponse) {
        val list = data.stateWiseData
        statesAdapter.submitList(list.subList(1, (list.size - 1)))
        viewBinder.stateDataRefresher.isRefreshing = false
    }

    override fun onRefresh() {
        viewModel.getStateData()
    }

    override fun onStateItemSelected(stateName: String) {
        viewModel.stateName = stateName
        findNavController().navigate(StatesDataFragmentDirections.actionStateDataFragmentToDistrictWiseDataFragment())
    }
}