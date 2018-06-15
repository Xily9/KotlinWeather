package com.xily.kotlinweather.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.BaseAdapter
import com.xily.kotlinweather.model.network.bean.WeatherBean
import com.xily.kotlinweather.utils.WeatherUtil
import com.xily.kotlinweather.utils.debug

class ForecastAdapter(mList: List<WeatherBean.ValueBean.WeathersBean>?) : BaseAdapter<ForecastAdapter.ViewHolder, WeatherBean.ValueBean.WeathersBean>(mList) {

    private val map = WeatherUtil.weatherIcons

    override val layoutId: Int
        get() = R.layout.layout_item_forecast

    override fun onBindViewHolder(holder: ViewHolder, position: Int, value: WeatherBean.ValueBean.WeathersBean) {
        holder.temperature.text = value.temp_day_c + "/" + value.temp_night_c + "°C"
        holder.weather.text = value.weather
        if (position == 0) {
            holder.day.text = "今天"
        } else {
            holder.day.text = value.week
        }
        if (map.containsKey(value.img)) {
            holder.icon.setImageResource(map[value.img]!!)
        } else {
            holder.icon.setImageResource(R.drawable.weather_na)
            debug("unknown", value.weather!! + value.img!!)
        }
    }

    class ViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        @BindView(R.id.day)
        lateinit var day: TextView
        @BindView(R.id.icon)
        lateinit var icon: ImageView
        @BindView(R.id.weather)
        lateinit var weather: TextView
        @BindView(R.id.temperature)
        lateinit var temperature: TextView
    }

}

