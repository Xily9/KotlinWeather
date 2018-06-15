package com.xily.kotlinweather.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.ui.fragment.HomePagerFragment

class HomePagerAdapter(fm: FragmentManager, private val cityList: List<CityListBean>?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return HomePagerFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return cityList?.size ?: 0
    }
}
