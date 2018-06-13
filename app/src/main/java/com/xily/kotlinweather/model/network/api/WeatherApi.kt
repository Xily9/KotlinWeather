package com.xily.kotlinweather.model.network.api

import com.xily.kotlinweather.model.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET(ApiConfig.guoLinApiUrl + "china/")
    fun getProvinces(): Observable<List<ProvincesBean>>

    @GET(ApiConfig.myApiUrl + "checkVersion2")
    fun checkVersion(): Observable<VersionBean>

    @GET(ApiConfig.meiZuApiUrl + "listWeather")
    fun getWeather(@Query("cityIds") cityId: String): Observable<WeatherBean>

    @GET(ApiConfig.heWeatherApiUrl + "find?key=5ddec80c2a44479083eccb0f5dcfba5b")
    fun search(@Query("location") location: String): Observable<SearchBean>

    @GET(ApiConfig.guoLinApiUrl + "china/{province}")
    fun getCities(@Path("province") province: String): Observable<List<CitiesBean>>

    @GET(ApiConfig.guoLinApiUrl + "china/{province}/{city}")
    fun getCounties(@Path("province") province: String, @Path("city") city: String): Observable<List<CountiesBean>>
}
