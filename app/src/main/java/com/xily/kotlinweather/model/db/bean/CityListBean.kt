package com.xily.kotlinweather.model.db.bean

import org.litepal.crud.LitePalSupport

data class CityListBean(var weatherId: Int, var cityName: String, var weatherData: String = "", var updateTime: Long = 0, var updateTimeStr: String = "") : LitePalSupport() {
    val id: Int = 0
}
