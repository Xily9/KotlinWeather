package com.xily.kotlinweather.model.network

import com.xily.kotlinweather.model.bean.*
import com.xily.kotlinweather.model.network.api.WeatherApi
import io.reactivex.Observable
import javax.inject.Inject


class RetrofitHelper @Inject
constructor(private val weatherApi: WeatherApi) : HttpHelper {

    override fun getProvinces(): Observable<List<ProvincesBean>> {
        return weatherApi.getProvinces()
    }

    override fun checkVersion(): Observable<VersionBean> {
        return weatherApi.checkVersion()
    }

    override fun getWeather(cityId: String): Observable<WeatherBean> {
        return weatherApi.getWeather(cityId)
    }

    override fun search(location: String): Observable<SearchBean> {
        return weatherApi.search(location)
    }

    override fun getCities(province: String): Observable<List<CitiesBean>> {
        return weatherApi.getCities(province)
    }

    override fun getCounties(province: String, city: String): Observable<List<CountiesBean>> {
        return weatherApi.getCounties(province, city)
    }
}
