package com.xily.kotlinweather.model.network

import com.xily.kotlinweather.model.bean.*
import io.reactivex.Observable


interface HttpHelper {

    fun getProvinces(): Observable<List<ProvincesBean>>

    fun checkVersion(): Observable<VersionBean>

    fun getWeather(cityId: String): Observable<WeatherBean>

    fun search(location: String): Observable<SearchBean>

    fun getCities(province: String): Observable<List<CitiesBean>>

    fun getCounties(province: String, city: String): Observable<List<CountiesBean>>
}
