package com.covidtracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidtracker.databinding.MainFragmentBinding
import com.covidtracker.dto.AllStateWiseResponse
import com.covidtracker.ui.adapters.SafeDataPagerAdapter
import com.covidtracker.util.getSafeData
import com.covidtracker.webservices.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.safeContentPager.adapter = SafeDataPagerAdapter(context!!, getSafeData(context!!))
        binding.swipeToRefreshLayout.setOnRefreshListener(this)

        observeRemoteData()
    }

    private fun observeRemoteData() {
        binding.stateDataNavigation.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToStateDataFragment())
        }
        viewModel.getStateData()
        viewModel.stateWiseLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ResponseState.Success -> {
                    binding.swipeToRefreshLayout.isRefreshing = false
                    setUiData(state.data)
                }
                is ResponseState.Failed -> {
                    binding.swipeToRefreshLayout.isRefreshing = false
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                }
                is ResponseState.Loading -> {
                    binding.swipeToRefreshLayout.isRefreshing = true
                }
            }
        })
    }

    private fun setUiData(data: AllStateWiseResponse) {
        val totalData = data.stateWiseData[0]
        binding.totalConfirmedCount.text = totalData.confirmed
        val deltaConfirmed = totalData.deltaConfirmed
        if (deltaConfirmed == "0")
            binding.confirmedCountGroup.visibility = View.GONE
        else {
            binding.confirmedCountGroup.visibility = View.VISIBLE
            binding.deltaConfirmedCount.text = "[+$deltaConfirmed]"
        }

        binding.totalActiveCount.text = totalData.active

        binding.totalRecoveredCount.text = totalData.recovered
        val deltaRecovered = totalData.deltaRecovered
        if (deltaRecovered == "0")
            binding.recoveredDeltaCountGroup.visibility = View.GONE
        else {
            binding.recoveredDeltaCountGroup.visibility = View.VISIBLE
            binding.deltaRecoveredCount.text = "[+$deltaRecovered]"
        }

        binding.totalDeceasedCount.text = totalData.deaths
        val deltaDeaths = totalData.deltaDeaths
        if (deltaDeaths == "0")
            binding.deceasedDeltaCountGroup.visibility = View.GONE
        else {
            binding.deceasedDeltaCountGroup.visibility = View.VISIBLE
            binding.deltaDeceasedCount.text = "[+$deltaDeaths]"
        }
    }

    /**
     * Refresh for latest data
     */
    override fun onRefresh() {
        viewModel.getStateData()
    }
}