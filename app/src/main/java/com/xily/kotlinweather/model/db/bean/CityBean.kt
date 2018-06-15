package com.xily.kotlinweather.model.db.bean

import org.litepal.crud.LitePalSupport

data class CityBean(var cityName: String,var cityCode: Int,var provinceId: Int) : LitePalSupport(){
    val id: Int = 0
}
