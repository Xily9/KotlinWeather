package com.xily.kotlinweather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.google.gson.Gson
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.model.network.bean.WeatherBean
import com.xily.kotlinweather.service.WeatherService
import com.xily.kotlinweather.ui.activity.MainActivity
import com.xily.kotlinweather.utils.PreferenceUtil
import com.xily.kotlinweather.utils.WeatherUtil
import com.xily.kotlinweather.utils.debug
import com.xily.kotlinweather.utils.isServiceRunning
import org.litepal.LitePal

class WeatherWidget : AppWidgetProvider() {
    private val map = WeatherUtil.weatherIcons

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        if (!isServiceRunning(BuildConfig.APPLICATION_ID + ".service.WeatherService")) {
            val startIntent = Intent(context, WeatherService::class.java)
            context.startService(startIntent)
        }
        val preferenceUtil = PreferenceUtil.instance
        val cityLists = LitePal.findAll(CityListBean::class.java)
        val cityId = preferenceUtil["notificationId", 0]
        var check = false
        for (cityList in cityLists) {
            if (cityList.id == cityId) {
                check = true
                break
            }
        }
        if (!check && !cityLists.isEmpty()) {
            preferenceUtil.put("notificationId", cityLists[0].id)
        }
        val cityList = LitePal.find(CityListBean::class.java, preferenceUtil["notificationId", 0].toLong())
        if (cityList != null) {
            val remoteViews = RemoteViews(context.packageName, R.layout.layout_widget)
            val weatherBean = Gson().fromJson<WeatherBean>(cityList.weatherData, WeatherBean::class.java)
            remoteViews.setTextViewText(R.id.cityName, cityList.cityName)
            if (weatherBean != null) {
                val valueBean = weatherBean.value!![0]
                remoteViews.setTextViewText(R.id.content, valueBean.realtime!!.weather + "   " + valueBean.pm25!!.aqi + " " + valueBean.pm25!!.quality + "   " + valueBean.realtime!!.wd + valueBean.realtime!!.ws)
                if (map.containsKey(valueBean.realtime!!.img)) {
                    remoteViews.setImageViewResource(R.id.icon, map[valueBean.realtime!!.img]!!)
                } else {
                    remoteViews.setImageViewResource(R.id.icon, R.drawable.weather_na)
                    debug("unknown", valueBean.realtime!!.weather!! + valueBean.realtime!!.img!!)
                }
                remoteViews.setTextViewText(R.id.temperature, valueBean.realtime!!.temp!! + "°")
            }
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            remoteViews.setOnClickPendingIntent(R.id.root, pendingIntent)
            for (appWidgetId in appWidgetIds) {
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}
