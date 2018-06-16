package com.xily.kotlinweather.model.db

import com.xily.kotlinweather.model.db.bean.LocationBean

interface ISQLiteHelper {
    fun getProvinces(): List<LocationBean>
    fun search(location: String): List<LocationBean>
    fun getCities(province: String): List<LocationBean>
    fun getCounties(province: String, city: String): List<LocationBean>
}