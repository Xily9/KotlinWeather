package com.xily.kotlinweather.ui.adapter

import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.BaseAdapter
import com.xily.kotlinweather.model.bean.WeatherBean

class AlarmAdapter(mList: List<WeatherBean.ValueBean.AlarmsBean>) : BaseAdapter<AlarmAdapter.ViewHolder, WeatherBean.ValueBean.AlarmsBean>(mList) {

    override val layoutId: Int
        get() = R.layout.layout_item_alarm

    override fun onBindViewHolder(holder: ViewHolder, position: Int, value: WeatherBean.ValueBean.AlarmsBean) {
        holder.alarm.text = value.alarmTypeDesc!! + "预警"
        holder.updateTime.text = value.publishTime
        holder.content.text = value.alarmContent
    }

    class ViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        @BindView(R.id.alarm)
        lateinit var alarm: TextView
        @BindView(R.id.updateTime)
        lateinit var updateTime: TextView
        @BindView(R.id.content)
        lateinit var content: TextView
    }

}

