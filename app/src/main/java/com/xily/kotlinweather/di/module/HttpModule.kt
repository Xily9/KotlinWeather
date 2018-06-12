package com.xily.kotlinweather.di.module

import com.xily.kotlinweather.model.network.api.WeatherApi

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class HttpModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://127.0.0.1/")//随便写一个,不写会报错
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    internal fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java!!)
    }
}
