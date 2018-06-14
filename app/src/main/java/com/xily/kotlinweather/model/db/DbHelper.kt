package com.xily.kotlinweather.model.db

import com.xily.kotlinweather.model.bean.*

interface DbHelper {
    val cityList: List<CityListBean>

    val province: List<ProvinceBean>

    fun getCityById(id: Int): CityListBean?

    fun getCityByWeatherId(id: Int): List<CityListBean>

    fun getCity(provinceId: String): List<CityBean>

    fun getCounty(cityId: String): List<CountyBean>

    fun deleteCity(id: Int)

    fun getAlarmsById(id: String): List<AlarmsBean>
}
