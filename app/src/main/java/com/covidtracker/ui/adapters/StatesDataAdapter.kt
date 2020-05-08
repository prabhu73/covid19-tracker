package com.covidtracker.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.covidtracker.databinding.LayoutStateItemBinding
import com.covidtracker.dto.StateWiseData
import com.covidtracker.util.getPeriod
import java.text.SimpleDateFormat

class StatesDataAdapter : ListAdapter<StateWiseData, StatesDataAdapter.StateDataViewHolder>(DIFF_CALLBACK) {

    private var onStateItemSelection: OnStateItemSelection? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StateWiseData>() {
            override fun areItemsTheSame(oldItem: StateWiseData, newItem: StateWiseData): Boolean =
                oldItem.state == newItem.state

            override fun areContentsTheSame(
                oldItem: StateWiseData,
                newItem: StateWiseData
            ): Boolean =
                oldItem == newItem
        }
    }

    fun setStateSelectionListener(onStateItemSelection: OnStateItemSelection) {
        this.onStateItemSelection = onStateItemSelection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StateDataViewHolder(
            LayoutStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: StateDataViewHolder, position: Int) {
        holder.viewBind(getItem(position))
    }

    inner class StateDataViewHolder(private val viewBinding: LayoutStateItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun viewBind(stateWiseData: StateWiseData) {
            viewBinding.stateName.text = stateWiseData.state
            viewBinding.confirmedCountText.text = stateWiseData.confirmed
            viewBinding.activeCountText.text = stateWiseData.active
            viewBinding.recoveredCountText.text = stateWiseData.recovered
            viewBinding.deceasedCountText.text = stateWiseData.deaths

            if (stateWiseData.lastUpdatedTime.isNotEmpty()) {
                viewBinding.lastUpdatedTimeText.visibility = View.VISIBLE
                viewBinding.lastUpdatedTimeText.text = "Last updated at: ${getPeriod(
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .parse(stateWiseData.lastUpdatedTime)!!)}"
            } else
                viewBinding.lastUpdatedTimeText.visibility = View.GONE

            if (stateWiseData.deltaConfirmed == "0") {
                viewBinding.confirmedDeltaCount.visibility = View.GONE
            } else {
                viewBinding.confirmedDeltaCount.visibility = View.VISIBLE
                viewBinding.confirmedDeltaCount.text = "[+${stateWiseData.deltaConfirmed}]"
            }

            if (stateWiseData.deltaRecovered == "0") {
                viewBinding.recoveredDeltaCount.visibility = View.GONE
            } else {
                viewBinding.recoveredDeltaCount.visibility = View.VISIBLE
                viewBinding.recoveredDeltaCount.text = "[+${stateWiseData.deltaRecovered}]"
            }

            if (stateWiseData.deltaDeaths == "0") {
                viewBinding.deceasedDeltaCount.visibility = View.GONE
            } else {
                viewBinding.deceasedDeltaCount.visibility = View.VISIBLE
                viewBinding.deceasedDeltaCount.text = "[+${stateWiseData.deltaDeaths}]"
            }

            viewBinding.stateItem.setOnClickListener {
                if (stateWiseData.confirmed != "0") onStateItemSelection?.onStateItemSelected(stateWiseData.state)
            }
        }
    }
}

interface OnStateItemSelection {
    fun onStateItemSelected(stateName: String)
}