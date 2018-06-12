package com.xily.kotlinweather.model.bean

import org.litepal.crud.LitePalSupport

data class CountyBean(var countyName: String, var weatherId: Int, var cityId: Int) : LitePalSupport() {
    val id: Int = 0
}
