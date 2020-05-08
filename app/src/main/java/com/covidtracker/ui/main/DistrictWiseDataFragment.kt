package com.covidtracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidtracker.databinding.FragmentDistrictWiseDataBinding
import com.covidtracker.dto.StateDistrictsData
import com.covidtracker.ui.adapters.DistrictWiseDataAdapter
import com.covidtracker.webservices.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class DistrictWiseDataFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewBinding: FragmentDistrictWiseDataBinding
    private val viewModel: MainViewModel by sharedViewModel()
    private val districtWiseDataAdapter = DistrictWiseDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDistrictWiseDataBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.districtRefreshData.setOnRefreshListener(this)
        viewBinding.districtWiseDetailsList.adapter = districtWiseDataAdapter
        observeFlowData()
    }

    private fun observeFlowData() {
        viewModel.getDistrictData()
        viewModel.districtWiseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseState.Success -> setUiData(it.data[0])
                is ResponseState.Failed -> viewBinding.districtRefreshData.isRefreshing = false
                is ResponseState.Loading -> viewBinding.districtRefreshData.isRefreshing = true
            }
        })

        viewBinding.backNavIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUiData(stateDistrictsData: StateDistrictsData) {
        viewBinding.stateName.text = stateDistrictsData.state
        val districtData = stateDistrictsData.districtData
        districtWiseDataAdapter.submitList(districtData.sortedWith(compareByDescending { it.confirmed }))
        viewBinding.districtRefreshData.isRefreshing = false
    }

    override fun onRefresh() {
        viewModel.getDistrictData()
    }
}