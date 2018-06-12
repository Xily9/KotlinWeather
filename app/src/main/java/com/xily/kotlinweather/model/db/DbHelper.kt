package com.xily.kotlinweather.model.db

import com.xily.kotlinweather.model.bean.AlarmsBean
import com.xily.kotlinweather.model.bean.CityBean
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.bean.CountyBean
import com.xily.kotlinweather.model.bean.ProvinceBean

interface DbHelper {
    val cityList: List<CityListBean>

    val province: List<ProvinceBean>

    fun getCityById(id: Int): CityListBean

    fun getCityByWeatherId(id: Int): List<CityListBean>

    fun getCity(provinceId: String): List<CityBean>

    fun getCounty(cityId: String): List<CountyBean>

    fun deleteCity(id: Int)

    fun getAlarmsById(id: String): List<AlarmsBean>
}
