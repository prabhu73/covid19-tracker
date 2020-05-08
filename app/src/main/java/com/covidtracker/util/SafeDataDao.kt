package com.covidtracker.util

import android.content.Context
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.covidtracker.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SafeData (
    @DrawableRes val image: Int,
    val info: String?
): Parcelable

fun getSafeData(context: Context): List<SafeData> {
    val images = context.resources.obtainTypedArray(R.array.safe_data_images)
    val infoData = context.resources.getStringArray(R.array.safe_data_info)
    val dataList = arrayListOf<SafeData>()
    infoData.forEachIndexed { index, i ->
        dataList.add(SafeData(images.getResourceId(index, -1), i))
    }
    images.recycle()
    return dataList
}