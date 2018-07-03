package com.xily.kotlinweather.service

import android.app.*
import android.content.*
import android.graphics.BitmapFactory
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import com.google.gson.Gson
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.di.component.DaggerServiceComponent
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.db.bean.AlarmsBean
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.model.network.bean.WeatherBean
import com.xily.kotlinweather.ui.activity.AlarmActivity
import com.xily.kotlinweather.ui.activity.MainActivity
import com.xily.kotlinweather.utils.WeatherUtil
import com.xily.kotlinweather.utils.debug
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import java.util.*
import javax.inject.Inject


class WeatherService : Service() {
    private var mMyBroadcastReceiver: MyBroadcastReceiver? = null
    private var pendingIntent: PendingIntent? = null
    private var isForeground: Boolean = false
    private var id = 2
    private val map = WeatherUtil.weatherIcons
    @Inject
    internal lateinit var mDataManager: DataManager

    private val notificationManager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val alarmManager: AlarmManager
        get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun onCreate() {
        super.onCreate()
        DaggerServiceComponent.builder()
                .appComponent(App.appComponent)
                .build().inject(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isAutoUpdate = mDataManager.autoUpdate
        val notification = mDataManager.notification
        if (notification) {
            startNotification(false)
        }
        if (isAutoUpdate) {
            runTask()
        }
        if (!notification && !isAutoUpdate) {
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
            stopSelf()
        } else {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BuildConfig.APPLICATION_ID + ".LOCAL_BROADCAST")
            mMyBroadcastReceiver = MyBroadcastReceiver()
            LocalBroadcastManager.getInstance(this).registerReceiver(mMyBroadcastReceiver!!, intentFilter)
            startTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {
        val i = Intent(this, WeatherService::class.java)
        pendingIntent = PendingIntent.getService(this, 0, i, 0)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30 * 60 * 1000, pendingIntent)
    }

    private fun startNotification(isUpdate: Boolean) {
        val cityList = mDataManager.getCityById(mDataManager.notificationId)
        cityList?.let {
            val weatherBean = Gson().fromJson<WeatherBean>(cityList.weatherData, WeatherBean::class.java)
            val intent1 = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0)
            val builder = NotificationCompat.Builder(this, "weather")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
            val remoteViews = RemoteViews(packageName, R.layout.layout_notification)
            remoteViews.setTextViewText(R.id.cityName, cityList.cityName)
            if (weatherBean != null) {
                val valueBean = weatherBean.value!![0]
                remoteViews.setTextViewText(R.id.content, valueBean.realtime!!.weather + "   " + valueBean.pm25!!.aqi + " " + valueBean.pm25!!.quality + "   " + valueBean.realtime!!.wd + valueBean.realtime!!.ws)
                if (map.containsKey(valueBean.realtime!!.img)) {
                    builder.setSmallIcon(map.get(valueBean.realtime!!.img)!!)
                    remoteViews.setImageViewResource(R.id.icon, map.get(valueBean.realtime!!.img)!!)
                } else {
                    builder.setSmallIcon(R.drawable.weather_na)
                    remoteViews.setImageViewResource(R.id.icon, R.drawable.weather_na)
                    debug("unknown", valueBean.realtime!!.weather!! + valueBean.realtime!!.img!!)
                }
                remoteViews.setTextViewText(R.id.temperature, valueBean.realtime!!.temp!! + "°")
            } else {
                builder.setContentText("N/A")
                builder.setSmallIcon(R.drawable.weather_na)
                remoteViews.setImageViewResource(R.id.icon, R.drawable.weather_na)
            }/*
            if (!TextUtils.isEmpty(cityList.getUpdateTimeStr())) {
                remoteViews.setTextViewText(R.id.updateTime, "更新于 " + cityList.getUpdateTimeStr());
            }*/
            builder.setCustomContentView(remoteViews)
            val notification = builder.build()
            if (isUpdate) {
                notificationManager.notify(1, notification)
            } else {
                startForeground(1, notification)
                isForeground = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMyBroadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyBroadcastReceiver!!)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun runTask() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val nightNoUpdate = mDataManager.nightNoUpdate
        if (nightNoUpdate && hour < 23 && hour >= 6 || !nightNoUpdate) {
            val cityList = mDataManager.cityList
            Observable.fromIterable(cityList)
                    .flatMap { cityList1 ->
                        mDataManager
                                .getWeather(cityList1.weatherId.toString())
                                .subscribeOn(Schedulers.io())
                                .doOnNext { weatherInfo ->
                                    val valueBean = weatherInfo.value!![0]
                                    val cityListUpdate = ContentValues()
                                    cityListUpdate.put("weatherData", Gson().toJson(weatherInfo))
                                    cityListUpdate.put("updateTime", System.currentTimeMillis())
                                    cityListUpdate.put("updateTimeStr", valueBean.realtime!!.time!!.substring(11, 16))
                                    LitePal.update(CityListBean::class.java, cityListUpdate, cityList1.id.toLong())
                                    if (mDataManager.alarm) {
                                        for (alarmsBean in valueBean.alarms!!) {
                                            val alarms = mDataManager.getAlarmsById(alarmsBean.alarmId!!)
                                            if (alarms.isEmpty()) {
                                                notificationManager.notify(id++, getNotification(cityList1.cityName + " " + alarmsBean.alarmTypeDesc + "预警", alarmsBean.alarmContent, cityList1.id))
                                                val alarmsBean1 = AlarmsBean(alarmsBean.alarmId!!)
                                                alarmsBean1.save()
                                            }
                                        }
                                    }
                                    if (mDataManager.rain) {
                                        val day = calendar.get(Calendar.YEAR).toString() + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH)
                                        val rainNotificationTime = mDataManager.rainNotificationTime
                                        if (hour > 6 && rainNotificationTime != day) {
                                            mDataManager.rainNotificationTime = day
                                            if (valueBean.weathers!![0].weather!!.contains("雨")) {
                                                notificationManager.notify(id++, getNotification(cityList1.cityName + "今天有雨", "今天天气为" + valueBean.weathers!![0].weather + ",出门记得带伞!"))
                                            }
                                        }
                                    }
                                    val notification = mDataManager.notification
                                    if (notification) {
                                        startNotification(true)
                                    }
                                    val intent = Intent("android.appwidget.action.APPWIDGET_UPDATE")
                                    sendBroadcast(intent)
                                }
                    }
                    .subscribe()
        }
    }

    private fun getNotification(title: String, content: String?, id: Int): Notification {
        return getNotification(title, content, 1, id)
    }

    private fun getNotification(title: String, content: String?, type: Int = 2, id: Int = 0): Notification {
        val intent: Intent
        if (type == 1) {
            debug("alarmId", "" + id)
            intent = Intent(this, AlarmActivity::class.java)
            intent.putExtra("alarmId", id)
        } else
            intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, "weather")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentText(content)
                .setAutoCancel(true)
                .build()
    }

    internal inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val isAutoUpdate = mDataManager.autoUpdate
            val notification = mDataManager.notification
            if (notification) {
                if (!isForeground)
                    startNotification(false)
                else
                    startNotification(true)
            } else {
                if (isForeground) {
                    stopForeground(true)
                    isForeground = false
                }
            }

            if (!notification && !isAutoUpdate) {
                if (pendingIntent != null) {
                    alarmManager.cancel(pendingIntent)
                }
                stopSelf()
            }
        }
    }
}
