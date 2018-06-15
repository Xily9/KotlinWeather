package com.xily.kotlinweather.ui.adapter

import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.BaseAdapter
import com.xily.kotlinweather.model.network.bean.WeatherBean

class SuggestAdapter(mList: List<WeatherBean.ValueBean.IndexesBean>?) : BaseAdapter<SuggestAdapter.ViewHolder, WeatherBean.ValueBean.IndexesBean>(mList) {

    override val layoutId: Int
        get() = R.layout.layout_item_suggest

    override fun onBindViewHolder(holder: ViewHolder, position: Int, value: WeatherBean.ValueBean.IndexesBean) {
        holder.name.text = value.name
        holder.value.text = value.level
    }

    class ViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        @BindView(R.id.name)
        lateinit var name: TextView
        @BindView(R.id.value)
        lateinit var value: TextView
    }

}

