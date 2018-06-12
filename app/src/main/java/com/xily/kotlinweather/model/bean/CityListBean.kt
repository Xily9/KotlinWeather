package com.xily.kotlinweather.model.bean

import org.litepal.crud.DataSupport
import org.litepal.crud.LitePalSupport

class CityListBean(weatherId: Int,cityName: String,weatherData: String,updateTime: Long,updateTimeStr: String) : LitePalSupport() {
    val id: Int = 0
}
