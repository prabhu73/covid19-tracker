package com.covidtracker.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.covidtracker.databinding.LayoutSafeDataItemBinding
import com.covidtracker.util.SafeData

class SafeDataPagerAdapter(
    private val context: Context,
    private val dataList: List<SafeData>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewBinder = LayoutSafeDataItemBinding.inflate(LayoutInflater.from(context), container, false)
        Glide.with(viewBinder.safeDataImage).load(dataList[position].image).into(viewBinder.safeDataImage)
        viewBinder.safeInfoData.text = dataList[position].info
        container.addView(viewBinder.root)
        return viewBinder.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}