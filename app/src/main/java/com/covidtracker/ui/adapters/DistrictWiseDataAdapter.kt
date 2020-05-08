package com.covidtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.covidtracker.databinding.LayoutDistrictDataItemBinding
import com.covidtracker.dto.DistrictData

class DistrictWiseDataAdapter :
    ListAdapter<DistrictData, DistrictWiseDataAdapter.DistrictDataViewHolder>(
        DIFF_CHECK
    ) {

    companion object {
        private val DIFF_CHECK = object : DiffUtil.ItemCallback<DistrictData>() {
            override fun areItemsTheSame(oldItem: DistrictData, newItem: DistrictData): Boolean =
                oldItem.district == newItem.district

            override fun areContentsTheSame(oldItem: DistrictData, newItem: DistrictData): Boolean =
                oldItem == newItem
        }
    }

    class DistrictDataViewHolder(private val viewBinder: LayoutDistrictDataItemBinding) :
        RecyclerView.ViewHolder(viewBinder.root) {
        fun viewBinding(districtData: DistrictData) {
            viewBinder.districtConfirmedCount.text = "${districtData.confirmed}"
            viewBinder.districtName.text = districtData.district
            val confirmed = districtData.deltaConfirmed.confirmed
            if (confirmed == 0) viewBinder.districtDeltaConfirmedCount.visibility = View.INVISIBLE
            else {
                viewBinder.districtDeltaConfirmedCount.visibility = View.VISIBLE
                viewBinder.districtDeltaConfirmedCount.text = "[+$confirmed]"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictDataViewHolder =
        DistrictDataViewHolder(
            LayoutDistrictDataItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DistrictDataViewHolder, position: Int) {
        holder.viewBinding(getItem(position))
    }
}