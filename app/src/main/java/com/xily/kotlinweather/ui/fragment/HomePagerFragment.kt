package com.xily.kotlinweather.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseFragment
import com.xily.kotlinweather.contract.PagerContract
import com.xily.kotlinweather.model.bean.WeatherBean
import com.xily.kotlinweather.presenter.PagerPresenter
import com.xily.kotlinweather.ui.activity.AlarmActivity
import com.xily.kotlinweather.ui.adapter.ForecastAdapter
import com.xily.kotlinweather.ui.adapter.SuggestAdapter
import com.xily.kotlinweather.utils.getAttrColor
import com.xily.kotlinweather.utils.showMessage
import com.xily.kotlinweather.widget.Weather3View
import java.util.*

class HomePagerFragment : RxBaseFragment<PagerPresenter>(), PagerContract.View {
    @BindView(R.id.temperature)
    internal lateinit var temperature: TextView
    @BindView(R.id.weather)
    internal lateinit var weather: TextView
    @BindView(R.id.air)
    internal lateinit var air: TextView
    @BindView(R.id.wet)
    internal lateinit var wet: TextView
    @BindView(R.id.wind)
    internal lateinit var wind: TextView
    @BindView(R.id.sendibleTemp)
    internal lateinit var sendibleTemp: TextView
    @BindView(R.id.alarm)
    internal lateinit var alarm: Button
    @BindView(R.id.forecast)
    internal lateinit var forecast: RecyclerView
    @BindView(R.id.pm25)
    internal lateinit var pm25: TextView
    @BindView(R.id.pm10)
    internal lateinit var pm10: TextView
    @BindView(R.id.so2)
    internal lateinit var so2: TextView
    @BindView(R.id.no2)
    internal lateinit var no2: TextView
    @BindView(R.id.co)
    internal lateinit var co: TextView
    @BindView(R.id.o3)
    internal lateinit var o3: TextView
    @BindView(R.id.weather3)
    internal lateinit var weather3View: Weather3View
    @BindView(R.id.suggest)
    internal lateinit var suggest: RecyclerView
    @BindView(R.id.layout_swipe_refresh)
    internal lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var title: TextView? = null
    private var updateTime: TextView? = null
    private var mIsVisible: Boolean = false
    private var isViewCreated: Boolean = false

    override val layoutId: Int
        get() = R.layout.layout_fragment_homepager

    override fun finishCreateView(state: Bundle?) {
        initToolbar()
        val bundle = arguments
        val position = bundle!!.getInt("position")
        mPresenter.getCityInfo(position)
        swipeRefreshLayout.setColorSchemeColors(activity!!.getAttrColor(R.attr.colorAccent))
        swipeRefreshLayout.setOnRefreshListener { mPresenter.getWeather(true) }
        isViewCreated = true
        onVisible()
        mPresenter.getWeather(false)
    }

    override fun initToolbar() {
        title = activity!!.findViewById(R.id.toolbar_title)
        updateTime = activity!!.findViewById(R.id.updateTime)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
        }
    }

    private fun onVisible() {
        if (mIsVisible && isViewCreated) {
            mPresenter.getSetTitleUpdateTime()
        }
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun showWeather(weatherBean: WeatherBean) {
        val valueBean = weatherBean.value!![0]
        temperature.text = valueBean.realtime!!.temp
        weather.text = valueBean.realtime!!.weather
        air.text = valueBean.pm25!!.aqi + " " + valueBean.pm25!!.quality
        wet.text = valueBean.realtime!!.sd!! + "%"
        wind.text = valueBean.realtime!!.wd!! + valueBean.realtime!!.ws!!
        sendibleTemp.text = valueBean.realtime!!.sendibleTemp!! + "°C"
        if (!valueBean.alarms!!.isEmpty()) {
            alarm.visibility = View.VISIBLE
            val list = ArrayList<String>()
            for (alarmsBean in valueBean.alarms!!) {
                list.add(alarmsBean.alarmTypeDesc!! + "预警")
            }
            val stringBuilder = StringBuilder()
            var isFirst = true
            for (str in list) {
                if (isFirst) {
                    stringBuilder.append(str)
                    isFirst = false
                } else {
                    stringBuilder.append(',')
                    stringBuilder.append(str)
                }
            }
            alarm.text = stringBuilder.toString()
            alarm.setOnClickListener {
                val intent = Intent(activity, AlarmActivity::class.java)
                intent.putExtra("alarmId", mPresenter!!.cityId)
                startActivity(intent)
            }
        }
        forecast.layoutManager = object : LinearLayoutManager(activity) {
            init {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        forecast.adapter = ForecastAdapter(valueBean.weathers)
        weather3View.setData(valueBean.weatherDetailsInfo!!.weather3HoursDetailsInfos!!)
        pm25.text = valueBean.pm25!!.pm25
        pm10.text = valueBean.pm25!!.pm10
        so2.text = valueBean.pm25!!.so2
        no2.text = valueBean.pm25!!.no2
        co.text = valueBean.pm25!!.co
        o3.text = valueBean.pm25!!.o3
        val indexesBeans = valueBean.indexes
        suggest.layoutManager = GridLayoutManager(activity, 3)
        suggest.adapter = SuggestAdapter(indexesBeans)
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }

    override fun setUpdateTime(updateTime: String) {
        this.updateTime!!.text = updateTime
    }

    override fun setTitle(title: String) {
        this.title!!.text = title
    }

    override fun sendBroadcast() {
        val intent = Intent(BuildConfig.APPLICATION_ID + ".LOCAL_BROADCAST")
        LocalBroadcastManager.getInstance(applicationContext!!).sendBroadcast(intent)
        val intent2 = Intent("android.appwidget.action.APPWIDGET_UPDATE")
        applicationContext!!.sendBroadcast(intent2)
    }

    override fun showErrorMsg(msg: String) {
        showMessage(activity!!.window.decorView, msg)
    }

    companion object {

        fun newInstance(position: Int): HomePagerFragment {
            val args = Bundle()
            args.putInt("position", position)
            val fragment = HomePagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
