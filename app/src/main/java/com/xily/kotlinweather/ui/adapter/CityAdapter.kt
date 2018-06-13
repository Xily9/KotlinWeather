package com.xily.kotlinweather.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.google.gson.Gson
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.BaseAdapter
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.bean.WeatherBean

class CityAdapter(mList: List<CityListBean>) : BaseAdapter<CityAdapter.ViewHolder, CityListBean>(mList) {

    override val layoutId: Int
        get() = R.layout.layout_item_city

    override fun onBindViewHolder(holder: ViewHolder, position: Int, value: CityListBean) {
        holder.cityName.text = value.cityName
        val data = value.weatherData
        if (TextUtils.isEmpty(data)) {
            holder.air.text = "N/A"
            holder.temperature.text = "N/A"
            holder.todayTemp.text = "N/A"
            holder.wet.text = "N/A"
            holder.wind.text = "N/A"
            holder.weather.text = "N/A"
        } else {
            val weatherBean = Gson().fromJson<WeatherBean>(data, WeatherBean::class.java!!)
            val valueBean = weatherBean.value!![0]
            holder.air.text = "空气质量" + valueBean.pm25!!.quality!!
            holder.weather.text = valueBean.realtime!!.weather
            holder.temperature.text = valueBean.realtime!!.temp!! + "°"
            holder.wind.text = valueBean.realtime!!.wd!! + valueBean.realtime!!.ws!!
            holder.todayTemp.text = valueBean.weathers!![0].temp_day_c + " / " + valueBean.weathers!![0].temp_night_c + "°"
            holder.wet.text = "湿度" + valueBean.realtime!!.sd + "%"
        }
    }

    class ViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        @BindView(R.id.cityName)
        lateinit var cityName: TextView
        @BindView(R.id.weather)
        lateinit var weather: TextView
        @BindView(R.id.temperature)
        lateinit var temperature: TextView
        @BindView(R.id.air)
        lateinit var air: TextView
        @BindView(R.id.wet)
        lateinit var wet: TextView
        @BindView(R.id.wind)
        lateinit var wind: TextView
        @BindView(R.id.todayTemp)
        lateinit var todayTemp: TextView
    }

}
