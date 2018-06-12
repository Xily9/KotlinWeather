package com.xily.kotlinweather.app

import android.app.Application
import com.xily.kotlinweather.di.component.AppComponent
import com.xily.kotlinweather.di.component.DaggerAppComponent
import com.xily.kotlinweather.di.module.AppModule
import com.xily.kotlinweather.di.module.HttpModule

import org.litepal.LitePal

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
        instance = this
    }

    companion object {
        lateinit var instance: App
        val appComponent: AppComponent
            get() = DaggerAppComponent.builder()
                    .appModule(AppModule(instance))
                    .httpModule(HttpModule())
                    .build()
    }
}
