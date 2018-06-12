package com.xily.kotlinweather.utils

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.xily.kotlinweather.app.App

object LocationUtil {

    fun getLocation(listener: AMapLocationListener) {
        val mLocationClient = AMapLocationClient(App.instance)
        mLocationClient.setLocationListener(listener)
        val mLocationOption = AMapLocationClientOption()
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.isOnceLocation = true
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }
}
