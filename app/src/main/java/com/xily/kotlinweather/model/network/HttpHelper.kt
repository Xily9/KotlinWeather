package com.xily.kotlinweather.model.network

import com.xily.kotlinweather.model.bean.CitiesBean
import com.xily.kotlinweather.model.bean.CountiesBean
import com.xily.kotlinweather.model.bean.ProvincesBean
import com.xily.kotlinweather.model.bean.SearchBean
import com.xily.kotlinweather.model.bean.VersionBean
import com.xily.kotlinweather.model.bean.WeatherBean

import rx.Observable

interface HttpHelper {

    fun getProvinces(): Observable<List<ProvincesBean>>

    fun checkVersion(): Observable<VersionBean>

    fun getWeather(cityId: String): Observable<WeatherBean>

    fun search(location: String): Observable<SearchBean>

    fun getCities(province: String): Observable<List<CitiesBean>>

    fun getCounties(province: String, city: String): Observable<List<CountiesBean>>
}
