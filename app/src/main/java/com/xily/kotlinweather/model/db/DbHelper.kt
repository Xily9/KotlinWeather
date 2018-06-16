package com.xily.kotlinweather.model.db

import com.xily.kotlinweather.model.db.bean.AlarmsBean
import com.xily.kotlinweather.model.db.bean.CityListBean

interface DbHelper {
    val cityList: List<CityListBean>

    fun getCityById(id: Int): CityListBean?

    fun getCityByWeatherId(id: Int): List<CityListBean>

    fun deleteCity(id: Int)

    fun getAlarmsById(id: String): List<AlarmsBean>
}
